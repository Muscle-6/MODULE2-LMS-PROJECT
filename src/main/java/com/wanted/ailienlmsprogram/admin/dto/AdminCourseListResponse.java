package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminCourseListResponse {

    private Long courseId;
    private String courseTitle;
    private String continentName;
    private String instructorName;
    private Integer coursePrice;
    private CourseStatus courseStatus;
    private LocalDateTime createdAt;

    public String getStatusLabel() {
        return courseStatus != null ? courseStatus.getDisplayName() : "-";
    }

    public CourseStatus getNextStatus() {
        if (courseStatus == null || courseStatus == CourseStatus.DRAFT) {
            return CourseStatus.PUBLISHED;
        }
        return CourseStatus.DRAFT;
    }

    public String getActionLabel() {
        if (courseStatus == null || courseStatus == CourseStatus.DRAFT) {
            return "공개 전환";
        }
        return "비공개 전환";
    }

    public boolean isDraft() {
        return courseStatus == CourseStatus.DRAFT;
    }
}