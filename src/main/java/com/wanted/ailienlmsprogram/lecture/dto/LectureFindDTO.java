package com.wanted.ailienlmsprogram.lecture.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LectureFindDTO {

    private Long lectureId;
    private Long courseId;
    private String lectureTitle;
    private String lectureDescription;
    private Integer lectureOrderIndex;
    private String videoUrl;
    private LocalDateTime createdAt;


}
