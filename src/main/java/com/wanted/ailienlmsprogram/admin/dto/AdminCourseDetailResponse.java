package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

//상세 페이지 전용 DTO
@Getter
@AllArgsConstructor
public class AdminCourseDetailResponse {

    private Long courseId;

    private String courseTitle;
    private String courseDescription;
    private String courseThumbnailUrl;
    private Integer coursePrice;
    private LocalDateTime createdAt;
    private CourseStatus courseStatus;

    private String continentName;
    private String continentDescription;

    private Long instructorId;
    private String instructorLoginId;
    private String instructorName;

    public String getStatusLabel() {
        return courseStatus != null ? courseStatus.getDisplayName() : "-";
    }

    public boolean isDraft() {
        return courseStatus == CourseStatus.DRAFT;
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
}