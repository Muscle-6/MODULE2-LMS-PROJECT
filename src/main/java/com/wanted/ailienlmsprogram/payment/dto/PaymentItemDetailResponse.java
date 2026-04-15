package com.wanted.ailienlmsprogram.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentItemDetailResponse {
    private Long paymentItemId;
    private Long courseId;
    private String courseTitle;
    private Integer itemPriceAtPurchase;
    private String instructorName;
}
