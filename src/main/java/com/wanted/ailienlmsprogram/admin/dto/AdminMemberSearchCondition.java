package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

/*관리자 회원 목록 검색 조건을 담는 DTO
*
* - query: 아이디,이름,이메일 검색어
* - role: 회원 역할 필터*/
@Getter
@Setter
public class AdminMemberSearchCondition {
    private String query;
    private Member.MemberRole role;
}