package com.wanted.ailienlmsprogram.lecture.service;

import com.wanted.ailienlmsprogram.lecture.dao.LectureRepository;
import com.wanted.ailienlmsprogram.lecture.dto.LectureFindDTO;
import com.wanted.ailienlmsprogram.lecture.dto.LectureResponseDTO;
import com.wanted.ailienlmsprogram.lecture.entity.Lecture;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final ModelMapper modelMapper;

    public List<LectureFindDTO> findMyLectures(Long courseId) {

        List<Lecture> lectures = lectureRepository.findByCourse_CourseId(courseId);

        return lectures.stream()
                .map(lecture -> modelMapper.map(lecture, LectureFindDTO.class))
                .collect(Collectors.toList());
    }

    // 강좌 상세 입장 시 뜨는 강의 목록
    public List<LectureResponseDTO> lectureByCourse(Long courseId) {

        List<Lecture> foundLectures = lectureRepository.findByCourse_CourseId(courseId);

        return foundLectures.stream()
                .map(lecture -> modelMapper.map(lecture, LectureResponseDTO.class))
                .collect(Collectors.toList());
    }
}
