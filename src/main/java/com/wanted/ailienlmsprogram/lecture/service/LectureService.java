package com.wanted.ailienlmsprogram.lecture.service;

import com.wanted.ailienlmsprogram.coursecommand.dao.CourseRepository;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.global.gcs.GcsService;
import com.wanted.ailienlmsprogram.lecture.dao.LectureRepository;
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
    private final GcsService gcsService;

    public List<LectureFindDTO> findMyLectures(Long courseId) {

        List<Lecture> lectures = lectureRepository.findByCourse_CourseId(courseId);

        return lectures.stream()
                .map(lecture -> modelMapper.map(lecture, LectureFindDTO.class))
                .collect(Collectors.toList());
    }


    @Transactional
    public void applyLecture(LectureAddDTO request, Long courseId, Long memberId, MultipartFile lectureVideo) throws IOException {

        // 1. 강좌 조회
        Course course = courseRepository.getReferenceById(courseId);

        // 2. 영상 GCS 업로드 (파일이 있을 때만)
        String videoUrl = null;
        if (lectureVideo != null && !lectureVideo.isEmpty()) {
            videoUrl = gcsService.uploadFile(lectureVideo, "lecture", memberId, courseId);
        }

        // 3. 순서 자동 계산 (현재 강의 수 + 1)
        int orderIndex = lectureRepository.countByCourse_CourseId(courseId) + 1;

        // 4. 엔티티 생성 후 저장
        Lecture lecture = Lecture.create(course, request.getLectureTitle(),
                request.getLectureDescription(), orderIndex, videoUrl);

        lectureRepository.save(lecture);
    }
  
    // 강좌 상세 입장 시 뜨는 강의 목록
    public List<LectureResponseDTO> lectureByCourse(Long courseId) {

        List<Lecture> foundLectures = lectureRepository.findByCourse_CourseId(courseId);

        return foundLectures.stream()
                .map(lecture -> modelMapper.map(lecture, LectureResponseDTO.class))
                .collect(Collectors.toList());
    }

    // 내 강좌 조회
    public List<LectureResponseDTO> viewMyLecture(Long courseId) {

        List<Lecture> foundLectures = lectureRepository.findAllByCourse_CourseId(courseId);

        return foundLectures.stream()
                .map(lecture -> modelMapper.map(lecture, LectureResponseDTO.class))
                .collect(Collectors.toList());

    }

    // 강의 수정 페이지
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

        // 2. 새 영상이 있을 때만 GCS 처리
        String videoUrl = null;
        if (lectureVideo != null && !lectureVideo.isEmpty()) {

            // 기존 영상이 GCS에 있으면 먼저 삭제
            if (lecture.getVideoUrl() != null) {
                gcsService.deleteFile(lecture.getVideoUrl());
            }

            // 새 영상 업로드
            videoUrl = gcsService.uploadFile(lectureVideo, "lecture", memberId, lectureId);
        }

        // 3. 엔티티 수정 메서드 호출
        //    → @Transactional + 더티체킹으로 save() 없이 자동 UPDATE
        lecture.editLectureInfo(
                request.getLectureTitle(),
                request.getLectureDescription(),
                videoUrl
        );
    }

    @Transactional
    public void deleteLecture(Long lectureId) {

        // 1. 강의 조회
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강의입니다."));

        // 2. GCS 영상 삭제 (영상이 있을 때만)
        if (lecture.getVideoUrl() != null) {
            gcsService.deleteFile(lecture.getVideoUrl());
        }

        lectureRepository.deleteById(lectureId);
    }

    public LectureWatchDTO findWatchLecture(Long lectureId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강의입니다."));

        return modelMapper.map(lecture, LectureWatchDTO.class);
    }
}
