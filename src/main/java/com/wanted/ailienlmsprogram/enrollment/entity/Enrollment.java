package com.wanted.ailienlmsprogram.enrollment.entity;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ENROLLMENT")
@Getter
@NoArgsConstructor
public class Enrollment {

    public enum EnrollmentStatus {
        ACTIVE, REFUNDED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_status", nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime enrolledAt;

    @Column(name = "enrollment_progress_rate")
    private BigDecimal enrollmentProgressRate;

    public void updateProgressRate(BigDecimal rate) {
        this.enrollmentProgressRate = rate;
    }

    // 생성 메서드
    public static Enrollment create(Member member, Course course) {
        Enrollment enrollment = new Enrollment();
        enrollment.member = member;
        enrollment.course = course;
        enrollment.status = EnrollmentStatus.ACTIVE;
        enrollment.enrolledAt = LocalDateTime.now();
        return enrollment;
    }

    // 재활성화 메서드 (환불 후 재수강)
    public void reActivate() {
        this.status = EnrollmentStatus.ACTIVE;
        this.enrolledAt = LocalDateTime.now();
    }

    // 환불 메서드
    public void refund() {
        this.status = EnrollmentStatus.REFUNDED;
    }
}
