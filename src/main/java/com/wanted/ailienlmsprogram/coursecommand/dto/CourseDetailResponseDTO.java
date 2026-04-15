package com.wanted.ailienlmsprogram.coursecommand.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseDetailResponseDTO {

    private String instructorId;
    private String instructorName;

    private Long courseId;
    private String courseTitle;
    private String courseDescription;
    private Integer coursePrice;

    private String courseStatus;

}
