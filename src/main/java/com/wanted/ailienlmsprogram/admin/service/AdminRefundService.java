package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminRefundListResponse;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.enrollment.service.EnrollmentService;
import com.wanted.ailienlmsprogram.payment.client.TossPaymentClient;
import com.wanted.ailienlmsprogram.payment.entity.Payment;
import com.wanted.ailienlmsprogram.payment.entity.PaymentItem;
import com.wanted.ailienlmsprogram.payment.entity.Refund;
import com.wanted.ailienlmsprogram.payment.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/*관리자 환불 처리 비즈니스 로직을 담당하는 서비스
*
* - 환불 요청 승인 처리
* - 환불 요청 거절 처리
* - 승인 시 결제에 포함된 강좌의 수강 취소 처리*/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminRefundService {

    private final RefundRepository refundRepository;
    private final EnrollmentService enrollmentService;
    private final TossPaymentClient tossPaymentClient;


    public List<AdminRefundListResponse> getRefunds(Refund.RefundStatus status){
        List<Refund> refunds = (status == null)
                ? refundRepository.findAllByOrderByRefundRequestedAtDesc()
                : refundRepository.findByRefundStatusOrderByRefundRequestedAtDesc(status);

        return refunds.stream().map(AdminRefundListResponse::from)
                .toList();
    }
    /**
     * REQUESTED 환불을 승인 처리한다.
     * - Toss 결제: cancel API 호출 후 수강 취소
     * - 서버사이드 결제: 수강 취소만 처리
     */
    @Transactional
    public void approveRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("환불 요청이 존재하지 않습니다."));

        if (refund.getRefundStatus() != Refund.RefundStatus.REQUESTED) {
            throw new IllegalArgumentException("처리 대기 중(REQUESTED) 상태의 환불만 승인할 수 있습니다.");
        }

        Payment payment = refund.getPayment();

        // Toss 결제인 경우 결제 취소 API 호출 (실패 시 예외 → 트랜잭션 롤백)
        String tossPaymentKey = payment.getTossPaymentKey();
        if (tossPaymentKey != null && !tossPaymentKey.isBlank()) {
            tossPaymentClient.cancel(tossPaymentKey, refund.getRefundReason());
        }

        refund.setRefundStatus(Refund.RefundStatus.APPROVED);
        refund.setRefundProcessedAt(LocalDateTime.now());

        // 결제에 포함된 강좌 수강 취소
        List<Course> courses = payment.getItems().stream()
                .map(PaymentItem::getCourse)
                .toList();
        enrollmentService.unenroll(refund.getMember(), courses);
    }

    /**
     * 서버사이드 결제의 REQUESTED 환불을 거절 처리한다.
     */

    //환불 요청을 거절 처리한다
    @Transactional
    public void rejectRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("환불 요청이 존재하지 않습니다."));

        if (refund.getRefundStatus() != Refund.RefundStatus.REQUESTED) {
            throw new IllegalArgumentException("처리 대기 중(REQUESTED) 상태의 환불만 거절할 수 있습니다.");
        }

        refund.setRefundStatus(Refund.RefundStatus.REJECTED);
        refund.setRefundProcessedAt(LocalDateTime.now());
    }
}
