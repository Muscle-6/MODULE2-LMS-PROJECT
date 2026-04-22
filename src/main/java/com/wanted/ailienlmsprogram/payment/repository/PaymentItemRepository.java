package com.wanted.ailienlmsprogram.payment.repository;

import com.wanted.ailienlmsprogram.payment.dto.PaymentItemDetailResponse;
import com.wanted.ailienlmsprogram.payment.entity.PaymentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentItemRepository extends JpaRepository<PaymentItem, Long> {

    @Query("""
            SELECT new com.wanted.ailienlmsprogram.payment.dto.PaymentItemDetailResponse(
                pi.paymentItemId,
                c.courseId,
                c.courseTitle,
                pi.itemPriceAtPurchase,
                instr.name
            )
            FROM PaymentItem pi
            JOIN pi.course c
            LEFT JOIN c.instructor instr
            WHERE pi.payment.paymentId = :paymentId
            """)
    List<PaymentItemDetailResponse> findDetailsByPaymentId(@Param("paymentId") Long paymentId);
}
