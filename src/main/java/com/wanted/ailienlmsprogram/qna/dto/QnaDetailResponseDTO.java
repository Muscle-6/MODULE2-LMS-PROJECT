package com.wanted.ailienlmsprogram.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QnaDetailResponseDTO {

    private Long qnaId;
    private String qnaTitle;
    private String qnaContent;
    private boolean isAnswered;
    private int replyCount;
    private String authorName;      // 추가
    private LocalDateTime createdAt; // 추가
    private Long memberId;
}
