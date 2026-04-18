package com.wanted.ailienlmsprogram.enrollment.repository;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.enrollment.entity.Enrollment;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByMemberMemberIdAndCourseCourseId(Long memberId, Long courseId);

    @Query(value = "SELECT e FROM Enrollment e WHERE e.member.memberId = :memberId AND e.status = 'ACTIVE'")
    List<Enrollment> findAllByMember_MemberId(@Param("memberId") Long studentId);


    boolean existsByMember_LoginIdAndCourse_CourseIdAndStatus(String memberLoginId, Long courseCourseId, Enrollment.EnrollmentStatus status);


    Optional<Enrollment> findByMember_LoginIdAndCourse_CourseId(String loginId, Long courseId);
}
