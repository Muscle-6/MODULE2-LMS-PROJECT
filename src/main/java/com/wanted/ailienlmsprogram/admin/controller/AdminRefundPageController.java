package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.service.AdminRefundService;
import com.wanted.ailienlmsprogram.payment.entity.Refund;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/* /admin/refunds 전용 컨트롤러 추가
* refund 목록 조회*/
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/refunds")
public class AdminRefundPageController {

    private final AdminRefundService adminRefundService;

    @GetMapping
    public String refundList(@RequestParam(required = false) Refund.RefundStatus status,
            Model model){
        model.addAttribute("refunds",adminRefundService.getRefunds(status));
        model.addAttribute("selectedStatus",status);
        model.addAttribute("refundStatus",Refund.RefundStatus.values());
        return "admin/refund-list";
    }
}
