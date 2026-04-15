package com.wanted.ailienlmsprogram.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserFindDTO {
    private String loginId;
    private String password;
    private String email;
    private String name;
    private String phone;
    private String profileImageUrl;
    private MemberRole role;
    private MemberRank rank;
    private String introduction;
}