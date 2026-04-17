package com.wanted.ailienlmsprogram.lecture.dao;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture,Long> {
    List<Lecture> findByCourse_CourseId(Long courseId);

    List<Lecture> findAllByCourse_CourseId(Long courseId);
}
