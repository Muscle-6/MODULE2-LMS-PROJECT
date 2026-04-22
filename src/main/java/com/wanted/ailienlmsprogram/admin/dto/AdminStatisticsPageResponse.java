package com.wanted.ailienlmsprogram.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/*통계 페이지 전체에 필요한 데이터를 한번에 전달하는 DTO.
*
* 포함 데이터:
* - 대륙 선택 옵션 목록
* - 현잿 선택된 대륙 정보
* - 대륙 랭킹 목록
* - 선택 대륙의 강좌 랭킹 목록*/
@Getter
@AllArgsConstructor
public class AdminStatisticsPageResponse {

    private List<AdminContinentOptionResponse> continentOptions;
    private Long selectedContinentId;
    private String selectedContinentName;
    private List<AdminContinentRankingResponse> continentRankings;
    private List<AdminCourseRankingResponse> courseRankings;
}