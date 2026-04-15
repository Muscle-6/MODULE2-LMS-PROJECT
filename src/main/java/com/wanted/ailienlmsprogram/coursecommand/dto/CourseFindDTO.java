package com.wanted.ailienlmsprogram.coursecommand.dto;

import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CourseFindDTO {

    private String courseTitle;
    private Long continent;
    private String courseDescription;
    private Integer coursePrice;
    private CourseStatus courseStatus;
    private LocalDateTime createdAt;

}
