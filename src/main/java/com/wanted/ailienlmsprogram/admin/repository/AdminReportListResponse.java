package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.community.entity.TargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminReportListResponse {

    private Long reportId;
    private TargetType targetType;
    private String reportReason;
    private LocalDateTime reportedAt;

    private String reporterLoginId;
    private String reporterName;

    private Long targetMemberId;
    private String targetMemberLoginId;
    private String targetMemberName;
    private String continentName;

    private String targetTitle;
    private String targetPreview;
    private boolean targetDeleted;

    public boolean hasTargetMember() {
        return targetMemberId != null;
    }

    public String getTargetTypeLabel() {
        return switch (targetType) {
            case CONTINENT_POST -> "게시글 신고";
            case CONTINENT_COMMENT -> "댓글 신고";
            case QNA -> "Q&A 신고";
        };
    }

    public String getTargetStatusLabel() {
        return targetDeleted ? "삭제/비공개" : "정상";
    }

    public String getDisplayReporterLoginId() {
        return isBlank(reporterLoginId) ? "-" : reporterLoginId;
    }

    public String getDisplayReporterName() {
        return isBlank(reporterName) ? "-" : reporterName;
    }

    public String getDisplayTargetMemberLoginId() {
        return isBlank(targetMemberLoginId) ? "-" : targetMemberLoginId;
    }

    public String getDisplayTargetMemberName() {
        return isBlank(targetMemberName) ? "-" : targetMemberName;
    }

    public String getDisplayContinentName() {
        return isBlank(continentName) ? "-" : continentName;
    }

    public String getDisplayTargetTitle() {
        return isBlank(targetTitle) ? "삭제되었거나 존재하지 않는 대상" : targetTitle;
    }

    public String getDisplayTargetPreview() {
        return isBlank(targetPreview) ? "-" : targetPreview;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}