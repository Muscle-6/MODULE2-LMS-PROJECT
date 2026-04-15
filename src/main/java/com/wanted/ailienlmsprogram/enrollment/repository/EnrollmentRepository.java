package com.wanted.ailienlmsprogram.enrollment.repository;

import com.wanted.ailienlmsprogram.enrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByMemberMemberIdAndCourseCourseId(Long memberId, Long courseId);
}
