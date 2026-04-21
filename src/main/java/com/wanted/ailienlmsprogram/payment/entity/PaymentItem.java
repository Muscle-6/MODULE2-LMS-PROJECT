package com.wanted.ailienlmsprogram.payment.entity;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PAYMENT_ITEM")
@Getter
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

    // 생성 메서드
    public static PaymentItem create(Payment payment, Course course) {
        PaymentItem item = new PaymentItem();
        item.payment = payment;
        item.course = course;
        item.itemPriceAtPurchase = course.getCoursePrice();
        return item;
    }
}
