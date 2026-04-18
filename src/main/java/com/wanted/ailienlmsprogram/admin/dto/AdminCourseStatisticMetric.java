package com.wanted.ailienlmsprogram.admin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*선택된 대륙 안에서 강좌 랭킹을 어떤 기준으로 볼지 결정한다.*/
@Getter
@RequiredArgsConstructor
public enum AdminCourseStatisticMetric {

    STUDENT_COUNT("학생 수 기준"),
    QNA_COUNT("Q&A 수 기준");

    private final String label;
}