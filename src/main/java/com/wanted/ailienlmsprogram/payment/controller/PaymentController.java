package com.wanted.ailienlmsprogram.payment.controller;

import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import com.wanted.ailienlmsprogram.payment.dto.RefundRequest;
import com.wanted.ailienlmsprogram.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public String paymentList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("payments", paymentService.getPayments(userDetails.getMember()));
        return "student/payment-list";
    }

    @GetMapping("/{paymentId}")
    public String paymentDetail(@PathVariable Long paymentId,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                Model model) {
        model.addAttribute("detail", paymentService.getPaymentDetail(paymentId, userDetails.getMember()));
        model.addAttribute("refundRequest", new RefundRequest());
        return "student/payment-detail";
    }

    @PostMapping("/{paymentId}/refund")
    public String requestRefund(@PathVariable Long paymentId,
                                @ModelAttribute RefundRequest refundRequest,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                RedirectAttributes ra) {
        try {
            paymentService.requestRefund(paymentId, refundRequest.getRefundReason(), userDetails.getMember());
            ra.addFlashAttribute("successMsg", "환불 요청이 접수되었습니다.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/student/payments/" + paymentId;
    }
}
