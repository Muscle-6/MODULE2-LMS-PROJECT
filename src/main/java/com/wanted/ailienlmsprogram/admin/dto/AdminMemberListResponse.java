package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminMemberListResponse {

    private Long memberId; // 내부 처리용
    private String loginId;
    private String name;
    private String email;
    private String phone;
    private Member.MemberRole role;
    private Member.MemberRank rank;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    private Member.AccountStatus accountStatus;
    private boolean deleted;

    public String getDisplayStatus() {
        if (deleted) {
            return "삭제됨";
        }
        return switch (accountStatus) {
            case ACTIVE -> "활성";
            case INACTIVE -> "비활성";
            case BANNED -> "제재";
        };
    }

    public boolean canToggleStatus() {
        return !deleted && role != Member.MemberRole.ADMIN;
    }

    public String getActionLabel() {
        if (deleted) {
            return "처리불가";
        }
        return accountStatus == Member.AccountStatus.ACTIVE ? "비활성화" : "활성화";
    }
}