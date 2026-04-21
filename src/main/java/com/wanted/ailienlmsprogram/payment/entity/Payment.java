package com.wanted.ailienlmsprogram.payment.entity;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "PAYMENT")
@Getter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "payment_order_no", nullable = false, unique = true, length = 50)
    private String paymentOrderNo;

    @Column(name = "payment_total_price", nullable = false)
    private Integer paymentTotalPrice;

    @Column(name = "payment_completed_at")
    private LocalDateTime paymentCompletedAt;

    @Column(name = "toss_payment_key", length = 200)
    private String tossPaymentKey;

    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    private List<PaymentItem> items;

    // 서버사이드 결제 생성 메서드
    public static Payment create(Member member, String orderNo, int totalPrice) {
        Payment payment = new Payment();
        payment.member = member;
        payment.paymentOrderNo = orderNo;
        payment.paymentTotalPrice = totalPrice;
        payment.paymentCompletedAt = LocalDateTime.now();
        return payment;
    }

    // 토스페이먼츠 결제 생성 메서드
    public static Payment createToss(Member member, String orderId, String paymentKey, int totalPrice) {
        Payment payment = new Payment();
        payment.member = member;
        payment.paymentOrderNo = orderId;
        payment.tossPaymentKey = paymentKey;
        payment.paymentTotalPrice = totalPrice;
        payment.paymentCompletedAt = LocalDateTime.now();
        return payment;
    }
}
