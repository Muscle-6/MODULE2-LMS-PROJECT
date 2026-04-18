package com.wanted.ailienlmsprogram.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 선택 대륙 안의 강좌 순위 한 줄을 표현한다.
@Getter
@AllArgsConstructor
public class AdminCourseRankingResponse {

    private String courseTitle;
    private String instructorName;
    private Long metricValue;

    public Long getMetricValueOrZero() {
        return metricValue == null ? 0L : metricValue;
    }
}