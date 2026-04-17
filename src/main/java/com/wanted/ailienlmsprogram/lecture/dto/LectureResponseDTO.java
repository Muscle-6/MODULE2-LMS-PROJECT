package com.wanted.ailienlmsprogram.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LectureResponseDTO {

    private Long courseId;

    private Long lectureId;
    private String lectureTitle;
    private String lectureDescription;
    private Integer lectureOrderIndex;


}
