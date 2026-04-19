package com.wanted.ailienlmsprogram.admin.dto;

import lombok.Getter;
import lombok.Setter;

// /admin/statics의 GET 파라미터를 한 객체로 묶는다.

/*RequestParam을 metric, selectedContinentId를 한 캑체로 묶으면
* Controller -> Service 전달이 훨씬 깔끔하다.*/
@Getter
@Setter
public class AdminStatisticsSearchCondition {

    private AdminContinentStatisticMetric continentMetric = AdminContinentStatisticMetric.COURSE_COUNT;
    private AdminCourseStatisticMetric courseMetric = AdminCourseStatisticMetric.STUDENT_COUNT;
    private Long selectedContinentId;
}