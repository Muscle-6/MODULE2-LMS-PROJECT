package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminMemberSearchCondition {
    private String query;
    private Member.MemberRole role;
}