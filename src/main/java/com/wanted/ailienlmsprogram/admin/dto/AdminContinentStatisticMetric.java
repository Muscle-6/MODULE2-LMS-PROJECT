package com.wanted.ailienlmsprogram.admin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*대륙 랭킹 기준을 고정된 값으로 관리한다.
*
* Controller가 요청 파라미터를 받을 때 바인딩되고, Repository가 어떤 집계 쿼리르 탈지 분기할 때 사용한다.*/
@Getter
@RequiredArgsConstructor
public enum AdminContinentStatisticMetric {

    COURSE_COUNT("강좌 수 기준"),
    STUDENT_COUNT("학생 수 기준"),
    POST_COUNT("게시판 글 수 기준"),
    REPORTER_COUNT("신고자 수 기준");

    private final String label;
}