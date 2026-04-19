package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*관리자 신고 목록 페이지 요청을 처리한다.
* -> AdminReportService*/
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/reports")
public class AdminReportController {

    private final AdminReportService adminReportService;

    @GetMapping
    public String reportList(Model model) {
        model.addAttribute("reports", adminReportService.getReports());
        return "admin/report-list";
    }
}