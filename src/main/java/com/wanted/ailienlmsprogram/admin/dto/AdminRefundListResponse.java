package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.payment.entity.Payment;
import com.wanted.ailienlmsprogram.payment.entity.Refund;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminRefundListResponse {

    private Long refundId;
    private Long paymentId;
    private String paymentOrderNo;
    private Integer paymentTotalPrice;

    private Long memberId;
    private String memberLoginId;
    private String memberName;

    private String refundReason;
    private Refund.RefundStatus refundStatus;
    private LocalDateTime refundRequestedAt;
    private LocalDateTime refundProcessedAt;

    public static AdminRefundListResponse from(Refund refund) {
        Payment payment = refund.getPayment();

        return new AdminRefundListResponse(
                refund.getRefundId(),
                payment.getPaymentId(),
                payment.getPaymentOrderNo(),
                payment.getPaymentTotalPrice(),
                refund.getMember().getMemberId(),
                refund.getMember().getLoginId(),
                refund.getMember().getName(),
                refund.getRefundReason(),
                refund.getRefundStatus(),
                refund.getRefundRequestedAt(),
                refund.getRefundProcessedAt()
        );
    }

    public boolean isRequested() {
        return refundStatus == Refund.RefundStatus.REQUESTED;
    }

    public String getDisplayStatus() {
        return switch (refundStatus) {
            case REQUESTED -> "처리 대기";
            case APPROVED -> "승인 완료";
            case REJECTED -> "거절 완료";
        };
    }
}