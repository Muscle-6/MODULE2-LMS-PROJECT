package com.wanted.ailienlmsprogram.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemResponse {
    private Long cartId;
    private Long courseId;
    private String courseTitle;
    private Integer price;
    private String instructorName;
}
