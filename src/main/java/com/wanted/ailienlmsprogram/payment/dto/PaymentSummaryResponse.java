package com.wanted.ailienlmsprogram.payment.dto;

import com.wanted.ailienlmsprogram.payment.entity.Refund;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentSummaryResponse {
    private Long paymentId;
    private String paymentOrderNo;
    private Integer paymentTotalPrice;
    private LocalDateTime paymentCompletedAt;
    private Long itemCount;
    private Refund.RefundStatus refundStatus; // 환불 내역 없으면 null
}
