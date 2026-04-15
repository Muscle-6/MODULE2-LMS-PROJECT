package com.wanted.ailienlmsprogram.payment.dto;

import com.wanted.ailienlmsprogram.payment.entity.Payment;
import com.wanted.ailienlmsprogram.payment.entity.Refund;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaymentDetailResponse {
    private Payment payment;
    private List<PaymentItemDetailResponse> items;
    private Refund refund; // 환불 내역 없으면 null
}
