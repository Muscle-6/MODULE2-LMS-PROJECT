package com.wanted.ailienlmsprogram.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TossPaymentResponse {
    private String paymentKey;    // Toss 결제 고유키
    private String orderId;       // 우리가 만든 주문번호
    private int totalAmount;      // 실제 결제된 금액
    private String status;        // "DONE" 이면 성공
    private String approvedAt;    // 실제 승인 시각
    private String method;        // 카드, 네이버페이 등
}