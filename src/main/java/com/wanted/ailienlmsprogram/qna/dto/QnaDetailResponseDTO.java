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
    private String authorName;
    private LocalDateTime createdAt;
    private Long memberId;

    private String answerContent;    // 답변 본문
    private LocalDateTime answeredAt; // 답변 시간 (선택사항)
}
