package com.wanted.ailienlmsprogram.coursecommand.dto;

import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CourseCommandDTO {

    private String courseTitle;
    private String courseDescription;
    private Long continentId;
    private Integer coursePrice;
    private CourseStatus courseStatus;

}
