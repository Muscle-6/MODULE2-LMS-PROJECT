package com.wanted.ailienlmsprogram.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private String content;
    private String memberName; // 작성자 이름
    private Long memberId;     // 본인 확인용 ID
}