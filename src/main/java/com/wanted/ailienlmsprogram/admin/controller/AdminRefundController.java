package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.service.AdminRefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/refunds")
@RequiredArgsConstructor
public class AdminRefundController {

    private final AdminRefundService adminRefundService;

    @PostMapping("/{refundId}/approve")
    public String approve(@PathVariable Long refundId, RedirectAttributes ra) {
        try {
            adminRefundService.approveRefund(refundId);
            ra.addFlashAttribute("successMsg", "환불이 승인되었습니다.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/refunds";
    }

    @PostMapping("/{refundId}/reject")
    public String reject(@PathVariable Long refundId, RedirectAttributes ra) {
        try {
            adminRefundService.rejectRefund(refundId);
            ra.addFlashAttribute("successMsg", "환불이 거절되었습니다.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/refunds";
    }
}
