package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/*관리자 강좌 목록 화면에 출력할 데이터를 담는 DTO
*
* - 강좌 기본 정보와 함께,
* - 화면에서 바로 사용할 상태 라벨/버튼 라벨 계산 메서드를 제공한다.*/
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

    // 현재 강좌 상태를 화면용 문자열로 변환한다,
    public String getStatusLabel() {
        return courseStatus != null ? courseStatus.getDisplayName() : "-";
    }

    // 현재 상태를 기준으로 다음 전환 대상 상태를 반환한다.
    public CourseStatus getNextStatus() {
        if (courseStatus == null || courseStatus == CourseStatus.DRAFT) {
            return CourseStatus.PUBLISHED;
        }
        return CourseStatus.DRAFT;
    }

    // 상태 전환 버튼에 표시할 문구를 반환한다.
    public String getActionLabel() {
        if (courseStatus == null || courseStatus == CourseStatus.DRAFT) {
            return "공개 전환";
        }
        return "비공개 전환";
    }

    // 현재 강좌가 비공개 초안 상태인지 여부를 반환한다.
    public boolean isDraft() {
        return courseStatus == CourseStatus.DRAFT;
    }
}