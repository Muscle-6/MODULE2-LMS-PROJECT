package com.wanted.ailienlmsprogram.coursecommand.dao;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {
}
