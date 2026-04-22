package com.wanted.ailienlmsprogram.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QnaModifyResponseDTO {

    private Long qnaId;
    private String qnaTitle;
    private String qnaContent;

    private Long courseId;

}
