package com.wanted.ailienlmsprogram.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QnaResponseDTO {

    private Long qnaId;          // 상세 페이지 이동을 위한 식별자
    private String title;        // 질문 제목
    private String content;      // 리스트에서 미리보기로 쓸 수도 있으니!

    // 작성자 정보 (일루미나티 테마니까 '요원명' 느낌으로!)
    private String authorName;
    private Long memberId;       // 본인 글인지 확인해서 수정/삭제 버튼 띄울 용도

    // 상태 및 날짜
    private String createdAt;    // LocalDateTime보다는 포맷팅된 String이 편해요 오호이야~!
    private boolean isAnswered;  // 답변이 달렸는지 여부 (parent_id 기반으로 계산)

    // 추가하면 좋은 '간지' 필드
    private int replyCount;
}
