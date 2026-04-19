package com.wanted.ailienlmsprogram.lecture.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LectureWatchDTO {

    private Long lectureId;
    private String lectureTitle;
    private String videoUrl;

}
