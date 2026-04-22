package com.wanted.ailienlmsprogram.lecture.dto;

import com.wanted.ailienlmsprogram.lecture.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Lecture.VideoStatus videoStatus;

}
