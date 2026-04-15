package com.wanted.ailienlmsprogram.coursecommand.dao;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findByContinent_ContinentId(Long continentId);

    // 상태별 조회 (공개/비공개 필터용)
    List<Course> findByInstructor_MemberIdAndCourseStatus(Long memberId, CourseStatus courseStatus);
}
