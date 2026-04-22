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
public class AdminContinentNoticeFormRequest {

    private Long postId;

    @NotNull(message = "대륙 정보가 없습니다.")
    private Long continentId;

    @NotBlank(message = "공지 제목을 입력해주세요.")
    @Size(max = 100, message = "공지 제목은 100자 이내로 입력해주세요.")
    private String postTitle;

    @NotBlank(message = "공지 내용을 입력해주세요.")
    @Size(max = 3000, message = "공지 내용은 3000자 이내로 입력해주세요.")
    private String postContent;
}