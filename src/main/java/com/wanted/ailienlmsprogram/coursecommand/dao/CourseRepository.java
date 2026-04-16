package com.wanted.ailienlmsprogram.coursecommand.dao;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    List<Course> findByContinent_ContinentId(Long continentId);

}
