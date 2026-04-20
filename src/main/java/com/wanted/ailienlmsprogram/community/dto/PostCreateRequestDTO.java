package com.wanted.ailienlmsprogram.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequestDTO {
    private String postTitle;   // 제목을 담을 칸
    private String postContent; // 내용을 담을 칸
    private Long continentId;   // 대륙 번호를 담을 칸
}