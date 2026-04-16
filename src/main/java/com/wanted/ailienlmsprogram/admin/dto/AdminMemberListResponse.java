package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/*관리자 회원 목록 화면에 필요한 데이터를 담는 응답 DTO.
*
* - 회원 기본 정보를 계쩡 상태를 포함하며,
* - 화면 표시를 돕기 위한 상태 문자열/버튼 활성화 관련 메소드를 제공한다.*/
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



}