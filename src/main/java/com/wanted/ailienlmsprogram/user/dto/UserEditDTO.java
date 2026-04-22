package com.wanted.ailienlmsprogram.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserEditDTO {

    private String name;
    private String phone;
    private String email;
    private String profileImageUrl;
    private String introduction;


}
