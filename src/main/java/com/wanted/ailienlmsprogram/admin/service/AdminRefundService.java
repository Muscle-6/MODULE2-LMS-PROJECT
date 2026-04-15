package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.course.entity.Course;
import com.wanted.ailienlmsprogram.enrollment.service.EnrollmentService;
import com.wanted.ailienlmsprogram.payment.entity.Payment;
import com.wanted.ailienlmsprogram.payment.entity.PaymentItem;
import com.wanted.ailienlmsprogram.payment.entity.Refund;
import com.wanted.ailienlmsprogram.payment.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminRefundService {

    private final RefundRepository refundRepository;
    private final EnrollmentService enrollmentService;

    /**
     * 서버사이드 결제의 REQUESTED 환불을 승인 처리한다.
     * 수강 취소(REFUNDED)까지 한 트랜잭션에서 처리.
     */
    @Transactional
    public void approveRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("환불 요청이 존재하지 않습니다."));

        if (refund.getRefundStatus() != Refund.RefundStatus.REQUESTED) {
            throw new IllegalArgumentException("처리 대기 중(REQUESTED) 상태의 환불만 승인할 수 있습니다.");
        }

        refund.setRefundStatus(Refund.RefundStatus.APPROVED);
        refund.setRefundProcessedAt(LocalDateTime.now());

        // 결제에 포함된 강좌 수강 취소
        Payment payment = refund.getPayment();
        List<Course> courses = payment.getItems().stream()
                .map(PaymentItem::getCourse)
                .toList();
        enrollmentService.unenroll(refund.getMember(), courses);
    }

    /**
     * 서버사이드 결제의 REQUESTED 환불을 거절 처리한다.
     */
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
