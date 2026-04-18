package com.wanted.ailienlmsprogram.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminContinentNoticeListResponse {

    private Long postId;
    private String postTitle;
    private String authorName;
    private LocalDateTime createdAt;
}