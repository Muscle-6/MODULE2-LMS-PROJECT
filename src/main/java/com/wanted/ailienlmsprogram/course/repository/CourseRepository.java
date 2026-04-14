package com.wanted.ailienlmsprogram.course.repository;

import com.wanted.ailienlmsprogram.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
