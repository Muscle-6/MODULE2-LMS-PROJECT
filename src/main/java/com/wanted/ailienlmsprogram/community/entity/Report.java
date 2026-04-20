package com.wanted.ailienlmsprogram.community.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportNo;

    @Column(name = "reporter_id")
    private Long reporterNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_target_type")
    private TargetType targetType;

    @Column(name = "report_target_id")
    private Long targetNo;

    @Column(name = "report_reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "created_at")
    private LocalDateTime reportDate = LocalDateTime.now();

    public static Report create(Long reporterNo, TargetType targetType, Long targetNo, String reason) {
        Report report = new Report();
        report.reporterNo = reporterNo;
        report.targetType = targetType;
        report.targetNo = targetNo;
        report.reason = reason;
        return report;
    }
}