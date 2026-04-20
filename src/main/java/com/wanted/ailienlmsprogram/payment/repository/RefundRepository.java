package com.wanted.ailienlmsprogram.payment.repository;

import com.wanted.ailienlmsprogram.payment.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    Optional<Refund> findByPaymentPaymentId(Long paymentId);

    boolean existsByPaymentPaymentIdAndRefundStatus(Long paymentId, Refund.RefundStatus refundStatus);

    /** 관리자: 특정 상태의 환불 목록 조회 (요청일 내림차순) */
    List<Refund> findByRefundStatusOrderByRefundRequestedAtDesc(Refund.RefundStatus refundStatus);

    /** 관리자: 전체 환불 목록 조회 (요청일 내림차순) */
    List<Refund> findAllByOrderByRefundRequestedAtDesc();

    /** 관리자: 전체 환불 목록 조회 - payment, member fetch join (N+1 방지) */
    @Query("SELECT r FROM Refund r JOIN FETCH r.payment JOIN FETCH r.member ORDER BY r.refundRequestedAt DESC")
    List<Refund> findAllWithPaymentAndMember();

    /** 관리자: 상태 필터링 환불 목록 조회 - payment, member fetch join (N+1 방지) */
    @Query("SELECT r FROM Refund r JOIN FETCH r.payment JOIN FETCH r.member WHERE r.refundStatus = :status ORDER BY r.refundRequestedAt DESC")
    List<Refund> findByStatusWithPaymentAndMember(@Param("status") Refund.RefundStatus status);
}
