package com.wanted.ailienlmsprogram.lecture.service;

import com.wanted.ailienlmsprogram.coursecommand.repository.CourseRepository;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.lecture.repository.LectureRepository;
import com.wanted.ailienlmsprogram.lecture.dto.LectureAddDTO;
import com.wanted.ailienlmsprogram.lecture.dto.LectureFindDTO;
import com.wanted.ailienlmsprogram.lecture.dto.LectureResponseDTO;
import com.wanted.ailienlmsprogram.lecture.dto.LectureWatchDTO;
import com.wanted.ailienlmsprogram.lecture.entity.Lecture;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private final LectureAsyncService lectureAsyncService;

    @Transactional(readOnly = true)
    public List<LectureFindDTO> findMyLectures(Long courseId) {

        List<Lecture> lectures = lectureRepository.findByCourseIdWithFetchJoin(courseId);

        return lectures.stream()
                .map(lecture -> modelMapper.map(lecture, LectureFindDTO.class))
                .collect(Collectors.toList());
    }


    @Transactional
    public void applyLecture(LectureAddDTO request, Long courseId, Long memberId, MultipartFile lectureVideo) throws IOException {

        // 1. 강좌 조회
        Course course = courseRepository.getReferenceById(courseId);

        // 2. 순서 자동 계산 (현재 강의 수 + 1)
        int orderIndex = lectureRepository.countByCourse_CourseId(courseId) + 1;

        // 3. 영상 없이 먼저 DB에 저장
        //    영상이 있으면 UPLOADING, 없으면 NONE 상태로 저장
        Lecture lecture = Lecture.create(course, request.getLectureTitle(),
                request.getLectureDescription(), orderIndex, null);

        if (lectureVideo != null && !lectureVideo.isEmpty()) {
            lecture.markAsUploading(); // UPLOADING 상태로 변경
        }

        lectureRepository.save(lecture);

        // 4. 영상이 있으면 요청 스레드에서 미리 byte[]로 복사 후 백그라운드로 던짐
        //    → MultipartFile은 HTTP 요청 생명주기에 종속되므로
        //      백그라운드 스레드에서 직접 접근하면 데이터가 사라져 오류 발생
        //    → byte[]로 JVM 힙 메모리에 복사해두면 요청 종료 후에도 안전하게 접근 가능
        if (lectureVideo != null && !lectureVideo.isEmpty()) {
            byte[] fileBytes = lectureVideo.getBytes();
            String contentType = lectureVideo.getContentType();
            String originalFilename = lectureVideo.getOriginalFilename();

            lectureAsyncService.uploadAndUpdateVideoUrl(
                    fileBytes, contentType, originalFilename,
                    "lecture", memberId, courseId, lecture.getLectureId()
            );
        }
    }
  
    // 강좌 상세 입장 시 뜨는 강의 목록
    @Transactional(readOnly = true)
    public List<LectureResponseDTO> lectureByCourse(Long courseId) {

        List<Lecture> foundLectures = lectureRepository.findByCourseIdWithFetchJoin(courseId);

        return foundLectures.stream()
                .map(lecture -> modelMapper.map(lecture, LectureResponseDTO.class))
                .collect(Collectors.toList());
    }

    // 내 강좌 조회
    @Transactional(readOnly = true)
    public List<LectureResponseDTO> viewMyLecture(Long courseId) {

        List<Lecture> foundLectures = lectureRepository.findAllByCourse_CourseId(courseId);

        return foundLectures.stream()
                .map(lecture -> modelMapper.map(lecture, LectureResponseDTO.class))
                .collect(Collectors.toList());

    }

    // 강의 수정 페이지
    @Transactional(readOnly = true)
    public LectureFindDTO findLectureById(Long lectureId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강의입니다."));

        return  modelMapper.map(lecture, LectureFindDTO.class);

    }

    @Transactional
    public void editLecture(LectureAddDTO request, Long lectureId, Long memberId, MultipartFile lectureVideo) throws IOException {

        // 1. 강의 조회
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강의입니다."));

        // 2. 본인 소유 강좌의 강의인지 확인
        if (!lecture.getCourse().getInstructor().getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인의 강의만 수정할 수 있습니다.");
        }

        // 2. 제목/설명 먼저 DB 업데이트 (더티체킹)
        //    videoUrl은 비동기 완료 후 업데이트되므로 여기선 건드리지 않음
        lecture.editLectureInfo(
                request.getLectureTitle(),
                request.getLectureDescription(),
                null  // videoUrl은 비동기에서 처리
        );

        // 3. 새 영상이 있으면 요청 스레드에서 미리 byte[]로 복사 후 백그라운드로 던짐
        if (lectureVideo != null && !lectureVideo.isEmpty()) {
            lecture.markAsUploading();
            byte[] fileBytes = lectureVideo.getBytes();
            String contentType = lectureVideo.getContentType();
            String originalFilename = lectureVideo.getOriginalFilename();
            String oldVideoUrl = lecture.getVideoUrl();

            if (oldVideoUrl != null) {
                // 기존 영상 있음 → 기존 파일 삭제 후 새 영상 업로드
                lectureAsyncService.deleteAndUploadAndUpdateVideoUrl(
                        fileBytes, contentType, originalFilename,
                        "lecture", memberId, lectureId, oldVideoUrl
                );
            } else {
                // 기존 영상 없음 (NONE 상태) → 바로 업로드
                lectureAsyncService.uploadAndUpdateVideoUrl(
                        fileBytes, contentType, originalFilename,
                        "lecture", memberId, lectureId, lectureId
                );
            }
        }
    }

    @Transactional
    public void deleteLecture(Long lectureId, Long memberId) {

        // 1. 강의 조회
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강의입니다."));

        // 2. 본인 소유 강좌의 강의인지 확인
        if (!lecture.getCourse().getInstructor().getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인의 강의만 삭제할 수 있습니다.");
        }

        // 3. GCS 영상 삭제 (영상이 있을 때만) → LectureAsyncService에 위임
        lectureAsyncService.deleteVideoFile(lecture.getVideoUrl());

        lectureRepository.deleteById(lectureId);
    }

    @Transactional(readOnly = true)
    public LectureWatchDTO findWatchLecture(Long lectureId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강의입니다."));

        return modelMapper.map(lecture, LectureWatchDTO.class);
    }
}
