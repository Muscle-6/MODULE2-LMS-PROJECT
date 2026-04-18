package com.wanted.ailienlmsprogram.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminContinentNoticeDetailResponse {

    private Long postId;
    private Long continentId;
    private String continentName;
    private String continentDescription;
    private String postTitle;
    private String postContent;
    private String authorName;
    private LocalDateTime createdAt;
}