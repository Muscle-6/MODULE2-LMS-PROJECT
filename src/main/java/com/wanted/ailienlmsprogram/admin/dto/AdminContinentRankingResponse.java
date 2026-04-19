package com.wanted.ailienlmsprogram.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*대륙 랭킹 테이블 한 줄을 표현하는 DTO.
*
* metricValue는 선택한 기준(강좌 수, 학생 수 등)의 실제 집계값이다.*/
@Getter
@AllArgsConstructor
public class AdminContinentRankingResponse {

    private Long continentId;
    private String continentName;
    private Long metricValue;

    /*null 집계값을 0으로 바꿔 화면/정렬 처리에 쉽게 만든다.*/
    public Long getMetricValueOrZero() {
        return metricValue == null ? 0L : metricValue;
    }
}