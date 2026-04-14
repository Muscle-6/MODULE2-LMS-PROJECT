package com.wanted.ailienlmsprogram.payment.controller;

import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import com.wanted.ailienlmsprogram.payment.entity.Payment;
import com.wanted.ailienlmsprogram.payment.service.CartService;
import com.wanted.ailienlmsprogram.payment.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final PaymentService paymentService;

    @Value("${toss.client-key}")
    private String tossClientKey;

    // ── 장바구니 조회 ────────────────────────────────────────────────────────────

    @GetMapping
    public String cartList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("cartItems", cartService.getCartItems(userDetails.getMember()));
        model.addAttribute("tossClientKey", tossClientKey);
        return "student/cart";
    }

    // ── 장바구니 추가 / 삭제 ─────────────────────────────────────────────────────

    @PostMapping("/{courseId}")
    public String addToCart(@PathVariable Long courseId,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            RedirectAttributes ra) {
        try {
            cartService.addToCart(courseId, userDetails.getMember());
            ra.addFlashAttribute("successMsg", "수강 바구니에 추가되었습니다.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/student/cart";
    }

    @PostMapping("/{cartId}/delete")
    public String removeFromCart(@PathVariable Long cartId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes ra) {
        try {
            cartService.removeFromCart(cartId, userDetails.getMember());
            ra.addFlashAttribute("successMsg", "항목이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/student/cart";
    }

    // ── 기존 서버사이드 결제 (Toss 미사용 fallback) ────────────────────────────

    @PostMapping("/checkout")
    public String checkoutAll(@AuthenticationPrincipal CustomUserDetails userDetails,
                              RedirectAttributes ra) {
        try {
            Payment payment = paymentService.checkoutAll(userDetails.getMember());
            ra.addFlashAttribute("successMsg", "결제가 완료되었습니다.");
            return "redirect:/student/payments/" + payment.getPaymentId();
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/student/cart";
        }
    }

    @PostMapping("/checkout/{cartId}")
    public String checkoutSingle(@PathVariable Long cartId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes ra) {
        try {
            Payment payment = paymentService.checkoutSingle(cartId, userDetails.getMember());
            ra.addFlashAttribute("successMsg", "결제가 완료되었습니다.");
            return "redirect:/student/payments/" + payment.getPaymentId();
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/student/cart";
        }
    }

    // ── 토스페이먼츠 결제 흐름 ─────────────────────────────────────────────────

    /**
     * [STEP 1] 결제 준비: 선택된 cartIds를 검증하고 orderId / amount / orderName을 생성.
     * orderId → cartIds 매핑을 세션에 저장한 뒤 JSON으로 반환.
     */
    @PostMapping("/toss/prepare")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> prepareTossPayment(
            @RequestBody Map<String, List<Long>> body,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpSession session) {
        try {
            List<Long> cartIds = body.get("cartIds");
            Map<String, Object> result = cartService.prepareTossPayment(cartIds, userDetails.getMember());

            // 세션에 orderId → cartIds 매핑 저장 (success 콜백에서 사용)
            String orderId = (String) result.get("orderId");
            session.setAttribute("toss_order_" + orderId, cartIds);

            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * [STEP 2] 결제 성공 콜백: 토스가 리다이렉트하는 URL.
     * 토스 서버 측 확인 → Payment 레코드 생성 → 결제 상세 페이지로 이동.
     */
    @GetMapping("/payment/success")
    public String tossSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam long amount,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpSession session,
            RedirectAttributes ra) {

        @SuppressWarnings("unchecked")
        List<Long> cartIds = (List<Long>) session.getAttribute("toss_order_" + orderId);
        session.removeAttribute("toss_order_" + orderId);

        if (cartIds == null) {
            ra.addFlashAttribute("errorMsg", "결제 정보를 찾을 수 없습니다. 다시 시도해주세요.");
            return "redirect:/student/cart";
        }

        try {
            Payment payment = paymentService.confirmTossPayment(
                    paymentKey, orderId, amount, cartIds, userDetails.getMember());
            ra.addFlashAttribute("successMsg", "결제가 완료되었습니다.");
            return "redirect:/student/payments/" + payment.getPaymentId();
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "결제 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/student/cart";
        }
    }

    /**
     * [STEP 2-fail] 결제 실패 콜백: 토스가 리다이렉트하는 URL.
     */
    @GetMapping("/payment/fail")
    public String tossFail(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String orderId,
            HttpSession session,
            RedirectAttributes ra) {

        if (orderId != null) {
            session.removeAttribute("toss_order_" + orderId);
        }
        ra.addFlashAttribute("errorMsg", "결제에 실패했습니다. " + (message != null ? message : ""));
        return "redirect:/student/cart";
    }
}
