package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/*관리자 회원 목록 화면에 출력할 데이터를 담는 DTO
*
* - 회원 기본 정보와 함께,
* - 화면에서 바로 사용할 상태 표시값과 액션 가능 여부를 계산한다.*/
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

    //계정 상태를 화면 표시용 문자열로 변환한다.
    public String getDisplayStatus() {
        return switch (accountStatus) {
            case ACTIVE -> "활성";
            case BANNED, INACTIVE -> "제재";
        };
    }

    //해당 회원의 상태 토글 버튼 노출 가능 여부를 반환한다.
    public boolean canToggleStatus() {
        return role != Member.MemberRole.ADMIN;
    }

    //현재 계정 상태를 기준으로 버튼 문구를 반환한다.
    public String getActionLabel() {
        return accountStatus == Member.AccountStatus.ACTIVE ? "제재" : "제재 해제";
    }


}