package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.service.AdminRefundService;
import com.wanted.ailienlmsprogram.payment.entity.Refund;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*관리자 환불 처리 요청을 담당하는 컨트롤러.
*
* - 관리자는 환불 목록 화면에서 특정 환불 요청을 승인하거나 거절할 수 있다.
* - 실제 비즈니스 로직은 서비스 계층에 위임.*/
@Controller
@RequestMapping("/admin/refunds")
@RequiredArgsConstructor
public class AdminRefundController {

    private final AdminRefundService adminRefundService;

    // 특정 환불 요청을 승인한다.
    @PostMapping("/{refundId}/approve")
    public String approve(@PathVariable Long refundId,
                          @RequestParam(required = false) Refund.RefundStatus status,
                          RedirectAttributes ra) {
        try {
            adminRefundService.approveRefund(refundId);
            ra.addFlashAttribute("successMsg", "환불이 승인되었습니다.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }

        if (status != null) {
            ra.addAttribute("status", status);
        }
        return "redirect:/admin/refunds";
    }

    //특정 환불 요청을 거절한다.
    @PostMapping("/{refundId}/reject")
    public String reject(@PathVariable Long refundId,
                         @RequestParam(required = false) Refund.RefundStatus status,
                         RedirectAttributes ra) {
        try {
            adminRefundService.rejectRefund(refundId);
            ra.addFlashAttribute("successMsg", "환불이 거절되었습니다.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }

        if (status != null) {
            ra.addAttribute("status", status);
        }
        return "redirect:/admin/refunds";
    }
}