package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminMemberListResponse {

    private Long memberId;
    private String loginId;
    private String name;
    private String email;
    private String phone;
    private Member.MemberRole role;
    private Member.MemberRank rank;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private Member.AccountStatus accountStatus;
    private LocalDateTime bannedAt;

    public String getDisplayStatus() {
        return switch (accountStatus) {
            case ACTIVE -> "활성";
            case BANNED, INACTIVE -> "제재";
        };
    }

    public boolean canToggleStatus() {
        return role != Member.MemberRole.ADMIN;
    }

    public String getActionLabel() {
        return accountStatus == Member.AccountStatus.ACTIVE ? "제재" : "제재 해제";
    }


}