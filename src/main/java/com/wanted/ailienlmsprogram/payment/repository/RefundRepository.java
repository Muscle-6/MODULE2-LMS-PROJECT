package com.wanted.ailienlmsprogram.payment.repository;

import com.wanted.ailienlmsprogram.payment.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    Optional<Refund> findByPaymentPaymentId(Long paymentId);

    boolean existsByPaymentPaymentIdAndRefundStatus(Long paymentId, Refund.RefundStatus refundStatus);
}
