package com.wanted.ailienlmsprogram.lecture.service;

import com.wanted.ailienlmsprogram.coursecommand.dao.CourseRepository;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.global.gcs.GcsService;
import com.wanted.ailienlmsprogram.lecture.dao.LectureRepository;
import com.wanted.ailienlmsprogram.lecture.dto.LectureAddDTO;
import com.wanted.ailienlmsprogram.lecture.dto.LectureFindDTO;
import com.wanted.ailienlmsprogram.lecture.entity.Lecture;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
        Lecture lecture = new Lecture(
                null,
                course,
                request.getLectureTitle(),
                request.getLectureDescription(),
                orderIndex,
                videoUrl,
                LocalDateTime.now()
        );

        lectureRepository.save(lecture);
    }
}
