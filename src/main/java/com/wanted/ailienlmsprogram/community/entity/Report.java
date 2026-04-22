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

    // --- 신고 당시의 데이터를 보존하기 위한 스냅샷 필드 ---
    @Column(name = "reported_title")
    private String reportedTitle;

    @Column(name = "reported_content", columnDefinition = "TEXT")
    private String reportedContent;
    // ------------------------------------------------

    @Column(name = "created_at")
    private LocalDateTime reportDate = LocalDateTime.now();

    /**
     * 신고 생성 메서드
     * @param title 신고 시점의 제목 (댓글일 경우 보통 null 또는 기본값)
     * @param content 신고 시점의 원본 내용 (박제용)
     */
    public static Report create(Long reporterNo, TargetType targetType, Long targetNo, String reason, String title, String content) {
        Report report = new Report();
        report.reporterNo = reporterNo;
        report.targetType = targetType;
        report.targetNo = targetNo;
        report.reason = reason;
        report.reportedTitle = title;
        report.reportedContent = content;
        return report;
    }
}