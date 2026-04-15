package com.wanted.ailienlmsprogram.enrollment.service;

import com.wanted.ailienlmsprogram.course.entity.Course;
import com.wanted.ailienlmsprogram.enrollment.entity.Enrollment;
import com.wanted.ailienlmsprogram.enrollment.repository.EnrollmentRepository;
import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    /**
     * 결제 완료 시 수강 등록.
     * 이미 ACTIVE 상태의 수강 내역이 있으면 중복 등록 방지.
     */
    @Transactional
    public void enroll(Member member, Course course) {
        boolean alreadyEnrolled = enrollmentRepository
                .findByMemberMemberIdAndCourseCourseId(member.getMemberId(), course.getCourseId())
                .filter(e -> e.getStatus() == Enrollment.EnrollmentStatus.ACTIVE)
                .isPresent();

        if (alreadyEnrolled) {
            return;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setMember(member);
        enrollment.setCourse(course);
        enrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollmentRepository.save(enrollment);
    }

    /**
     * 환불 시 수강 취소.
     * 해당 결제에 포함된 강좌들의 ENROLLMENT 상태를 REFUNDED로 변경.
     */
    @Transactional
    public void unenroll(Member member, List<Course> courses) {
        for (Course course : courses) {
            enrollmentRepository
                    .findByMemberMemberIdAndCourseCourseId(member.getMemberId(), course.getCourseId())
                    .ifPresent(e -> e.setStatus(Enrollment.EnrollmentStatus.REFUNDED));
        }
    }
}
