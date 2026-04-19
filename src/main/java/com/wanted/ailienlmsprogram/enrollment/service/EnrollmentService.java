package com.wanted.ailienlmsprogram.enrollment.service;

import com.wanted.ailienlmsprogram.coursecommand.dto.CourseFindResponseDTO;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.enrollment.dto.EnrollmentResponseDTO;
import com.wanted.ailienlmsprogram.enrollment.entity.Enrollment;
import com.wanted.ailienlmsprogram.enrollment.repository.EnrollmentRepository;
import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ModelMapper modelMapper;

    /**
     * 결제 완료 시 수강 등록.
     * 이미 ACTIVE 상태의 수강 내역이 있으면 중복 등록 방지.
     */
    @Transactional
    public void enroll(Member member, Course course) {
        Enrollment enrollment = enrollmentRepository
                .findByMemberMemberIdAndCourseCourseId(member.getMemberId(), course.getCourseId())
                .orElse(null);

        if (enrollment != null) {
            // 이미 ACTIVE면 중복 등록 방지
            if (enrollment.getStatus() == Enrollment.EnrollmentStatus.ACTIVE) {
                return;
            }
            // REFUNDED면 새로 INSERT 대신 기존 row 재활용!
            enrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);
            enrollment.setEnrolledAt(LocalDateTime.now());
            // save 불필요 - @Transactional이 자동으로 UPDATE 처리
        } else {
            // 최초 수강이면 새로 생성
            enrollment = new Enrollment();
            enrollment.setMember(member);
            enrollment.setCourse(course);
            enrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);
            enrollment.setEnrolledAt(LocalDateTime.now());
            enrollmentRepository.save(enrollment);
        }
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

    // 내가 수강 중인 강좌 조회
    public List<EnrollmentResponseDTO> enrollmentsByMemberId(Long studentId) {
        List<Enrollment> enrollmentList = enrollmentRepository.findAllByMember_MemberId(studentId);

        return enrollmentList.stream()
                .map(enrollment -> {
                    EnrollmentResponseDTO dto = modelMapper.map(enrollment, EnrollmentResponseDTO.class);
                    if (enrollment.getCourse().getInstructor() != null) {
                        dto.setInstructorName(enrollment.getCourse().getInstructor().getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 수강 여부 확인 로직
    public boolean isStudentEnrolled(String loginId, Long courseId) {
        if (loginId == null) return false;
        // 에러 나는 경우 - loginId 뒤에 쉼표가 줄 끝에 있으면 IDE에 따라 빨간줄
        return enrollmentRepository.existsByMember_LoginIdAndCourse_CourseIdAndStatus(
                loginId,
                courseId,
                Enrollment.EnrollmentStatus.ACTIVE
        );
    }

    public Long findEnrollmentId(String loginId, Long courseId) {
        return enrollmentRepository
                .findByMember_LoginIdAndCourse_CourseId(loginId, courseId)
                .map(Enrollment::getEnrollmentId)
                .orElse(null);
    }

    public BigDecimal getProgressRate(String loginId, Long courseId) {
        return enrollmentRepository
                .findByMember_LoginIdAndCourse_CourseId(loginId, courseId)
                .map(Enrollment::getEnrollmentProgressRate)
                .orElse(BigDecimal.ZERO);
    }
}
