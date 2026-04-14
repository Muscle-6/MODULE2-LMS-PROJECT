package com.wanted.ailienlmsprogram.payment.repository;

import com.wanted.ailienlmsprogram.payment.dto.PaymentSummaryResponse;
import com.wanted.ailienlmsprogram.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * 결제 목록 + 각 결제의 강좌 수 + 환불 상태를 한 번에 조회.
     * refundStatus: 환불 내역 없으면 null, 있으면 Refund.RefundStatus enum.
     */
    @Query("""
            SELECT new com.wanted.ailienlmsprogram.payment.dto.PaymentSummaryResponse(
                p.paymentId,
                p.paymentOrderNo,
                p.paymentTotalPrice,
                p.paymentCompletedAt,
                COUNT(pi),
                (SELECT r.refundStatus FROM Refund r WHERE r.payment = p)
            )
            FROM Payment p
            LEFT JOIN p.items pi
            WHERE p.member.memberId = :memberId
            GROUP BY p.paymentId, p.paymentOrderNo, p.paymentTotalPrice, p.paymentCompletedAt
            ORDER BY p.paymentCompletedAt DESC
            """)
    List<PaymentSummaryResponse> findSummariesByMemberId(@Param("memberId") Long memberId);

    Optional<Payment> findByPaymentIdAndMemberMemberId(Long paymentId, Long memberId);
}
