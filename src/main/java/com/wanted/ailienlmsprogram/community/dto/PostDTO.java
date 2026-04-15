package com.wanted.ailienlmsprogram.community.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostDTO {
    private Long postId;
    private Long continentId; // 어느 대륙의 게시판인지 구분
    private String title;
    private String content;
    private String authorName;
}
