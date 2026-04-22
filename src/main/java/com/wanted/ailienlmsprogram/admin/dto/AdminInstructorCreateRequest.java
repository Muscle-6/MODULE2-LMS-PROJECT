package com.wanted.ailienlmsprogram.admin.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/*관리자 강사 계정 생성 요청 데이터를 담는 DTO
*
* - 강사 생성 폼에서 입력받은 아이디, 이메일, 비밀번호, 이름을 전달하며,
* - Bean Validation을 통해 필수값 및 이메일 형식을 검증한다.*/
@Getter
@Setter
public class AdminInstructorCreateRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;
}
