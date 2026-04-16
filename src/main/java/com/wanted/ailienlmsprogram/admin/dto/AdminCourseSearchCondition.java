package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import lombok.Getter;
import lombok.Setter;

/*강좌 목록/검색용 DTO(Request)*/
@Getter
@Setter
public class AdminCourseSearchCondition {
    private String query;          // 검색어
    private CourseStatus status;   // DRAFT / PUBLISHED / null(전체)
}
