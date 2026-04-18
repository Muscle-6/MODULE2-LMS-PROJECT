package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.*;
import com.wanted.ailienlmsprogram.admin.repository.AdminStatisticsQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/*통계 페이지에 필요한 전체 흐름을 조립한다.
*
* 기본 metric 세팅, 선택 대륙 없으면 첫 대륙 자동 선택 같은 화면 흐름*/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStatisticsService {

    private final AdminStatisticsQueryRepository adminStatisticsQueryRepository;

    public AdminStatisticsPageResponse getStatisticsPage(AdminStatisticsSearchCondition condition) {
        normalizeCondition(condition);

        List<AdminContinentOptionResponse> continentOptions = adminStatisticsQueryRepository.findContinentOptions();
        Long selectedContinentId = resolveSelectedContinentId(condition.getSelectedContinentId(), continentOptions);
        condition.setSelectedContinentId(selectedContinentId);

        String selectedContinentName = findSelectedContinentName(selectedContinentId, continentOptions);

        List<AdminContinentRankingResponse> continentRankings =
                adminStatisticsQueryRepository.findContinentRankings(condition.getContinentMetric());

        List<AdminCourseRankingResponse> courseRankings =
                selectedContinentId == null
                        ? Collections.emptyList()
                        : adminStatisticsQueryRepository.findCourseRankings(selectedContinentId, condition.getCourseMetric());

        return new AdminStatisticsPageResponse(
                continentOptions,
                selectedContinentId,
                selectedContinentName,
                continentRankings,
                courseRankings
        );
    }

    private void normalizeCondition(AdminStatisticsSearchCondition condition) {
        if (condition.getContinentMetric() == null) {
            condition.setContinentMetric(AdminContinentStatisticMetric.COURSE_COUNT);
        }

        if (condition.getCourseMetric() == null) {
            condition.setCourseMetric(AdminCourseStatisticMetric.STUDENT_COUNT);
        }
    }

    private Long resolveSelectedContinentId(
            Long selectedContinentId,
            List<AdminContinentOptionResponse> continentOptions
    ) {
        if (continentOptions == null || continentOptions.isEmpty()) {
            return null;
        }

        if (selectedContinentId != null) {
            for (AdminContinentOptionResponse option : continentOptions) {
                if (option.getContinentId().equals(selectedContinentId)) {
                    return selectedContinentId;
                }
            }
        }

        return continentOptions.get(0).getContinentId();
    }

    private String findSelectedContinentName(
            Long selectedContinentId,
            List<AdminContinentOptionResponse> continentOptions
    ) {
        if (selectedContinentId == null || continentOptions == null) {
            return null;
        }

        for (AdminContinentOptionResponse option : continentOptions) {
            if (option.getContinentId().equals(selectedContinentId)) {
                return option.getContinentName();
            }
        }

        return null;
    }
}