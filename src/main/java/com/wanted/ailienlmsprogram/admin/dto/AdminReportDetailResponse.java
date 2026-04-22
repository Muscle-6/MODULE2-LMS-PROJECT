package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.community.entity.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminReportDetailResponse {

    private Long reportId;
    private TargetType targetType;
    private String reportReason;
    private LocalDateTime reportedAt;

    private String reporterLoginId;
    private String reporterName;

    private Long targetMemberId;
    private String targetMemberLoginId;
    private String targetMemberName;

    /**
     * 게시글/댓글이면 "대륙", Q&A면 "강좌" 식으로 라벨을 분기하기 위한 값
     */
    private String categoryLabel;

    /**
     * 게시글/댓글이면 대륙명, Q&A면 강좌명
     */
    private String categoryValue;

    /**
     * 게시글/Q&A는 제목, 댓글은 "댓글"
     */
    private String targetTitle;

    /**
     * 실제 본문/댓글 내용/Q&A 본문
     */
    private String targetContent;

    /**
     * 댓글이면 원문 게시글 제목, Q&A 답변이면 부모 질문 제목
     */
    private String parentTitle;

    private boolean targetDeleted;

    public boolean hasTargetMember() {
        return targetMemberId != null;
    }

    public boolean hasParentTitle() {
        return !isBlank(parentTitle);
    }

    public boolean canDelete() {
        return !targetDeleted;
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

    public String getDisplayCategoryLabel() {
        return isBlank(categoryLabel) ? "분류" : categoryLabel;
    }

    public String getDisplayCategoryValue() {
        return isBlank(categoryValue) ? "-" : categoryValue;
    }

    public String getDisplayTargetTitle() {
        return isBlank(targetTitle) ? "삭제되었거나 존재하지 않는 대상" : targetTitle;
    }

    public String getDisplayTargetContent() {
        return isBlank(targetContent) ? "-" : targetContent;
    }

    public String getDisplayParentTitle() {
        return isBlank(parentTitle) ? "-" : parentTitle;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}