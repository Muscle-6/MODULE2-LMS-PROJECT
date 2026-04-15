package com.wanted.ailienlmsprogram.payment.entity;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "PAYMENT")
@Getter
@Setter
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

    /** 토스페이먼츠 paymentKey — 환불(cancel) 호출 시 사용. 서버사이드 결제는 null. */
    @Column(name = "toss_payment_key", length = 200)
    private String tossPaymentKey;

    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    private List<PaymentItem> items;
}
