package com.wanted.ailienlmsprogram.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminStatisticsPageResponse {

    private List<AdminContinentOptionResponse> continentOptions;
    private Long selectedContinentId;
    private String selectedContinentName;
    private List<AdminContinentRankingResponse> continentRankings;
    private List<AdminCourseRankingResponse> courseRankings;
}