package com.wanted.ailienlmsprogram.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminContinentNoticeWriteRequest {

    @NotNull(message = "대륙 정보가 없습니다.")
    private Long continentId;

    @NotBlank(message = "공지 제목을 입력해주세요.")
    @Size(max = 200, message = "공지 제목은 200자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "공지 내용을 입력해주세요.")
    private String content;
}