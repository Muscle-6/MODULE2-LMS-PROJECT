package com.wanted.ailienlmsprogram.lecture.dao;


import com.wanted.ailienlmsprogram.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture,Long> {
    //List<Lecture> findByCourse_CourseId(Long courseId);

    @Query("select l from lecture l " +
            "join fetch l.course " +
            "where l.course.courseId = :courseId")
    List<Lecture> findByCourseIdWithFetchJoin(@Param("courseId") Long courseId);

    List<Lecture> findAllByCourse_CourseId(Long courseId);
    int countByCourse_CourseId(Long courseId);

}
