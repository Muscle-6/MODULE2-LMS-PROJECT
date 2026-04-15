package com.wanted.ailienlmsprogram.payment.repository;

import com.wanted.ailienlmsprogram.payment.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    Optional<Refund> findByPaymentPaymentId(Long paymentId);

    boolean existsByPaymentPaymentIdAndRefundStatus(Long paymentId, Refund.RefundStatus refundStatus);

    /** 관리자: 특정 상태의 환불 목록 조회 (요청일 내림차순) */
    List<Refund> findByRefundStatusOrderByRefundRequestedAtDesc(Refund.RefundStatus refundStatus);

    /** 관리자: 전체 환불 목록 조회 (요청일 내림차순) */
    List<Refund> findAllByOrderByRefundRequestedAtDesc();
}
