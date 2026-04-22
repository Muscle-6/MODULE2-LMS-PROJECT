package com.wanted.ailienlmsprogram.lecture.dto;

import com.wanted.ailienlmsprogram.lecture.entity.Lecture;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LectureWatchDTO {

    private Long lectureId;
    private String lectureTitle;
    private String lectureDescription;
    private String videoUrl;
    private Lecture.VideoStatus videoStatus;

}
