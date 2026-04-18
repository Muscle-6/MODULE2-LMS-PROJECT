package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.dto.AdminContinentStatisticMetric;
import com.wanted.ailienlmsprogram.admin.dto.AdminCourseStatisticMetric;
import com.wanted.ailienlmsprogram.admin.dto.AdminStatisticsPageResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminStatisticsSearchCondition;
import com.wanted.ailienlmsprogram.admin.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/statics")
public class AdminStatisticsController {

    private final AdminStatisticsService adminStatisticsService;

    @GetMapping
    public String staticsPage(
            AdminStatisticsSearchCondition condition,
            Model model
    ) {
        AdminStatisticsPageResponse page = adminStatisticsService.getStatisticsPage(condition);

        model.addAttribute("condition", condition);
        model.addAttribute("page", page);
        model.addAttribute("continentMetricValues", AdminContinentStatisticMetric.values());
        model.addAttribute("courseMetricValues", AdminCourseStatisticMetric.values());

        return "admin/statics";
    }
}