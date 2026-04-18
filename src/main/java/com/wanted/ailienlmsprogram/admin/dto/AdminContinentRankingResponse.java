package com.wanted.ailienlmsprogram.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminContinentRankingResponse {

    private Long continentId;
    private String continentName;
    private Long metricValue;

    public Long getMetricValueOrZero() {
        return metricValue == null ? 0L : metricValue;
    }
}