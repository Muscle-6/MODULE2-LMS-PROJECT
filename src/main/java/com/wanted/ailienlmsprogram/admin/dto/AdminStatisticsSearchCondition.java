package com.wanted.ailienlmsprogram.admin.dto;

import lombok.Getter;
import lombok.Setter;

// /admin/statics의 GET 파라미터를 한 객체로 묶는다.
@Getter
@Setter
public class AdminStatisticsSearchCondition {

    private AdminContinentStatisticMetric continentMetric = AdminContinentStatisticMetric.COURSE_COUNT;
    private AdminCourseStatisticMetric courseMetric = AdminCourseStatisticMetric.STUDENT_COUNT;
    private Long selectedContinentId;
}