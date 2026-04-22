package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*관리자 신고 목록 페이지 요청을 처리한다.
* -> AdminReportService*/
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/reports")
public class AdminReportController {

    private final AdminReportService adminReportService;

    //신고 목록 페이지 조회
    @GetMapping
    public String reportList(Model model) {
        model.addAttribute("reports", adminReportService.getReports());
        return "admin/report-list";
    }

    //신고 상세 페이지 조회
    @GetMapping("/{reportId}")
    public String reportDetail(@PathVariable long reportId, Model model) {
        model.addAttribute("report",adminReportService.getReportDetail(reportId));
        return "admin/report-detail";
    }

    //신고 대상 삭제 처리
    @PostMapping("/{reportId}/delete")
    public String deleteReport(@PathVariable long reportId,
                               RedirectAttributes redirectAttributes) {
        adminReportService.deleteReportedTarget(reportId);
        redirectAttributes.addFlashAttribute("message","신고 대상 삭제가 완료되었습니다.");
        return "redirect:/admin/reports";
    }

}