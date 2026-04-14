package com.wanted.ailienlmsprogram.payment.service;

import com.wanted.ailienlmsprogram.enrollment.service.EnrollmentService;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.payment.client.TossPaymentClient;
import com.wanted.ailienlmsprogram.payment.dto.PaymentDetailResponse;
import com.wanted.ailienlmsprogram.payment.dto.PaymentSummaryResponse;
import com.wanted.ailienlmsprogram.payment.entity.Cart;
import com.wanted.ailienlmsprogram.payment.entity.Payment;
import com.wanted.ailienlmsprogram.payment.entity.PaymentItem;
import com.wanted.ailienlmsprogram.payment.entity.Refund;
import com.wanted.ailienlmsprogram.payment.repository.CartRepository;
import com.wanted.ailienlmsprogram.payment.repository.PaymentItemRepository;
import com.wanted.ailienlmsprogram.payment.repository.PaymentRepository;
import com.wanted.ailienlmsprogram.payment.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentItemRepository paymentItemRepository;
    private final CartRepository cartRepository;
    private final RefundRepository refundRepository;
    private final EnrollmentService enrollmentService;
    private final TossPaymentClient tossPaymentClient;

    public List<PaymentSummaryResponse> getPayments(Member member) {
        return paymentRepository.findSummariesByMemberId(member.getMemberId());
    }

    public PaymentDetailResponse getPaymentDetail(Long paymentId, Member member) {
        Payment payment = paymentRepository.findByPaymentIdAndMemberMemberId(paymentId, member.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("결제 내역이 존재하지 않거나 접근 권한이 없습니다."));

        return new PaymentDetailResponse(
                payment,
                paymentItemRepository.findDetailsByPaymentId(paymentId),
                refundRepository.findByPaymentPaymentId(paymentId).orElse(null)
        );
    }

    @Transactional
    public Payment checkoutAll(Member member) {
        List<Cart> cartItems = cartRepository.findByMemberMemberId(member.getMemberId());
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }
        return processCheckout(cartItems, member);
    }

    @Transactional
    public Payment checkoutSingle(Long cartId, Member member) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        if (!cart.getMember().getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException("본인의 장바구니 항목만 결제할 수 있습니다.");
        }

        return processCheckout(List.of(cart), member);
    }

    private Payment processCheckout(List<Cart> cartItems, Member member) {
        int totalPrice = cartItems.stream()
                .mapToInt(c -> c.getCourse().getPrice())
                .sum();

        String orderNo = "ORD-"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-"
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

        Payment payment = new Payment();
        payment.setMember(member);
        payment.setPaymentOrderNo(orderNo);
        payment.setPaymentTotalPrice(totalPrice);
        payment.setPaymentCompletedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        for (Cart cart : cartItems) {
            PaymentItem item = new PaymentItem();
            item.setPayment(payment);
            item.setCourse(cart.getCourse());
            item.setItemPriceAtPurchase(cart.getCourse().getPrice());
            paymentItemRepository.save(item);

            // TODO: EnrollmentService 구현 완료 후 실제 수강 등록 처리
            enrollmentService.enroll(member, cart.getCourse());
        }

        cartRepository.deleteAll(cartItems);
        return payment;
    }

    /**
     * 토스페이먼츠 결제 확인 후 Payment/PaymentItem 레코드를 생성한다.
     * orderId는 토스에서 검증된 값이므로 paymentOrderNo로 직접 사용한다.
     */
    @Transactional
    public Payment confirmTossPayment(String paymentKey, String orderId, long amount,
                                      List<Long> cartIds, Member member) {
        // 1. 토스페이먼츠 서버 측 결제 확인
        tossPaymentClient.confirm(paymentKey, orderId, amount);

        // 2. 장바구니 항목 로드 및 소유권 검증
        List<Cart> cartItems = cartRepository.findAllById(cartIds);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("결제할 강좌를 찾을 수 없습니다.");
        }

        boolean allBelongToMember = cartItems.stream()
                .allMatch(c -> c.getMember().getMemberId().equals(member.getMemberId()));
        if (!allBelongToMember) {
            throw new IllegalArgumentException("접근 권한이 없는 항목이 포함되어 있습니다.");
        }

        // 3. Payment 생성 (orderId를 paymentOrderNo로 사용)
        int totalPrice = cartItems.stream().mapToInt(c -> c.getCourse().getPrice()).sum();

        Payment payment = new Payment();
        payment.setMember(member);
        payment.setPaymentOrderNo(orderId);
        payment.setPaymentTotalPrice(totalPrice);
        payment.setPaymentCompletedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // 4. PaymentItem 생성 + 수강 등록
        for (Cart cart : cartItems) {
            PaymentItem item = new PaymentItem();
            item.setPayment(payment);
            item.setCourse(cart.getCourse());
            item.setItemPriceAtPurchase(cart.getCourse().getPrice());
            paymentItemRepository.save(item);

            // TODO: EnrollmentService 구현 완료 후 실제 수강 등록 처리
            enrollmentService.enroll(member, cart.getCourse());
        }

        // 5. 장바구니 비우기
        cartRepository.deleteAll(cartItems);
        return payment;
    }

    @Transactional
    public void requestRefund(Long paymentId, String refundReason, Member member) {
        Payment payment = paymentRepository.findByPaymentIdAndMemberMemberId(paymentId, member.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("결제 내역이 존재하지 않거나 접근 권한이 없습니다."));

        if (refundRepository.existsByPaymentPaymentIdAndRefundStatus(paymentId, Refund.RefundStatus.REQUESTED)) {
            throw new IllegalArgumentException("이미 환불 요청이 진행 중입니다.");
        }

        Refund refund = new Refund();
        refund.setPayment(payment);
        refund.setMember(member);
        refund.setRefundReason(refundReason);
        refund.setRefundStatus(Refund.RefundStatus.REQUESTED);
        refund.setRefundRequestedAt(LocalDateTime.now());
        refundRepository.save(refund);
    }
}
