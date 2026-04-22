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

    /*통계 페이지 전체 응답을 만든다.
    * selectedContinentId가 없거나 잘못된 값이면
    * 첫 번째 대륙을 자동 선택하는 흐름까지 이 메서드가 처리한다.*/
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

    /*통계 기준값이 비어 있을 때 기본 metric을 세팅한다.*/
    private void normalizeCondition(AdminStatisticsSearchCondition condition) {
        if (condition.getContinentMetric() == null) {
            condition.setContinentMetric(AdminContinentStatisticMetric.COURSE_COUNT);
        }

        if (condition.getCourseMetric() == null) {
            condition.setCourseMetric(AdminCourseStatisticMetric.STUDENT_COUNT);
        }
    }

    /*전달받은 selectedContinentId가 유효한지 검사한다.
    * 유효하지 않으면 첫 번째 대륙 ID를 반환한다.*/
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

    /*현재 선택된 대륙 ID에 대응하는 대륙명을 찾는다.*/
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