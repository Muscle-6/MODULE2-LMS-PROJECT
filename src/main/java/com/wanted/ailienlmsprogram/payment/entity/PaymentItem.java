package com.wanted.ailienlmsprogram.payment.entity;

import com.wanted.ailienlmsprogram.course.entity.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PAYMENT_ITEM")
@Getter
@Setter
@NoArgsConstructor
public class PaymentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_item_id")
    private Long paymentItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "item_price_at_purchase", nullable = false)
    private Integer itemPriceAtPurchase;
}
