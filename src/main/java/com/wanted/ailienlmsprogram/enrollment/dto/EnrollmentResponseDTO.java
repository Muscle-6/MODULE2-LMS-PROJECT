package com.wanted.ailienlmsprogram.enrollment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EnrollmentResponseDTO {

    private Long enrollmentId;
    private String courseTitle;
    private LocalDateTime enrolledAt;
    private String status;

    private String instructorName;

}
