package com.wanted.ailienlmsprogram.enrollment.entity;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ENROLLMENT")
@Getter
@Setter
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
}
