package com.wanted.ailienlmsprogram.payment.service;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.enrollment.service.EnrollmentService;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.payment.client.TossPaymentClient;
import com.wanted.ailienlmsprogram.payment.dto.PaymentDetailResponse;
import com.wanted.ailienlmsprogram.payment.dto.PaymentSummaryResponse;
import com.wanted.ailienlmsprogram.payment.dto.TossPaymentResponse;
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
                .mapToInt(c -> c.getCourse().getCoursePrice())
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
            item.setItemPriceAtPurchase(cart.getCourse().getCoursePrice());
            paymentItemRepository.save(item);

            enrollmentService.enroll(member, cart.getCourse());
        }

        cartRepository.deleteAll(cartItems);
        return payment;
    }

    /**
     * 토스페이먼츠 결제 확인.
     * 1. 금액 위변조 검증 (DB 가격 합산 vs 클라이언트 전달 금액)
     * 2. 토스 서버 confirm API 호출
     * 3. Payment / PaymentItem 생성 + 수강 등록
     */
    @Transactional
    public Payment confirmTossPayment(String paymentKey, String orderId, long amount,
                                      List<Long> cartIds, Member member) {
        // 1. 장바구니 항목 로드 및 소유권 검증
        List<Cart> cartItems = cartRepository.findAllById(cartIds);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("결제할 강좌를 찾을 수 없습니다.");
        }

        boolean allBelongToMember = cartItems.stream()
                .allMatch(c -> c.getMember().getMemberId().equals(member.getMemberId()));
        if (!allBelongToMember) {
            throw new IllegalArgumentException("접근 권한이 없는 항목이 포함되어 있습니다.");
        }

        // 2. 금액 위변조 검증 — DB 기준 가격 합산과 클라이언트 전달 금액 비교
        int expectedAmount = cartItems.stream().mapToInt(c -> c.getCourse().getCoursePrice()).sum();
        if (expectedAmount != (int) amount) {
            throw new IllegalArgumentException(
                    "결제 금액이 일치하지 않습니다. (예상: " + expectedAmount + "원, 수신: " + amount + "원)");
        }


        // 3. 토스페이먼츠 서버 측 결제 확인 (금액 검증 통과 후 1회만 호출)
        TossPaymentResponse response = tossPaymentClient.confirm(paymentKey, orderId, amount);

        if (response == null) {
            throw new IllegalArgumentException("토스페이먼츠 응답을 받지 못했습니다.");
        }
        if (!"DONE".equals(response.getStatus())) {
            throw new IllegalArgumentException("결제가 완료되지 않았습니다. (status: " + response.getStatus() + ")");
        }
        if (response.getTotalAmount() != expectedAmount) {
            throw new IllegalArgumentException("Toss 응답 금액이 일치하지 않습니다.");
        }
        if (!response.getOrderId().equals(orderId)) {
            throw new IllegalArgumentException("Toss 응답 주문번호가 일치하지 않습니다.");
        }




        // 4. Payment 생성
        Payment payment = new Payment();
        payment.setMember(member);
        payment.setPaymentOrderNo(orderId);
        payment.setTossPaymentKey(paymentKey);
        payment.setPaymentTotalPrice(expectedAmount);
        payment.setPaymentCompletedAt(LocalDateTime.now());
        paymentRepository.save(payment);



        // 5. PaymentItem 생성 + 수강 등록
        for (Cart cart : cartItems) {
            PaymentItem item = new PaymentItem();
            item.setPayment(payment);
            item.setCourse(cart.getCourse());
            item.setItemPriceAtPurchase(cart.getCourse().getCoursePrice());
            paymentItemRepository.save(item);

            enrollmentService.enroll(member, cart.getCourse());
        }

        // 6. 장바구니 비우기
        cartRepository.deleteAll(cartItems);
        return payment;
    }

    /**
     * 환불 요청.
     * - Toss 결제: cancel API 호출 → APPROVED 저장 + 수강 취소
     * - 서버사이드 결제: REQUESTED 저장 → 관리자 수동 승인 대기
     */
    @Transactional
    public void requestRefund(Long paymentId, String refundReason, Member member) {
        Payment payment = paymentRepository.findByPaymentIdAndMemberMemberId(paymentId, member.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("결제 내역이 존재하지 않거나 접근 권한이 없습니다."));

        if (refundRepository.findByPaymentPaymentId(paymentId).isPresent()) {
            throw new IllegalArgumentException("이미 환불이 요청되었거나 처리된 결제입니다.");
        }

        Refund refund = new Refund();
        refund.setPayment(payment);
        refund.setMember(member);
        refund.setRefundReason(refundReason);
        refund.setRefundRequestedAt(LocalDateTime.now());

        String tossPaymentKey = payment.getTossPaymentKey();

        if (tossPaymentKey != null && !tossPaymentKey.isBlank()) {
            // Toss cancel API 호출 (실패 시 예외 → 트랜잭션 롤백)
            tossPaymentClient.cancel(tossPaymentKey, refundReason);
            refund.setRefundStatus(Refund.RefundStatus.APPROVED);
            refund.setRefundProcessedAt(LocalDateTime.now());

            // 수강 취소
            List<Course> courses = payment.getItems().stream()
                    .map(PaymentItem::getCourse)
                    .toList();
            enrollmentService.unenroll(member, courses);
        } else {
            // 서버사이드 결제 → 관리자 수동 승인 대기
            refund.setRefundStatus(Refund.RefundStatus.REQUESTED);
        }

        refundRepository.save(refund);
    }
}
