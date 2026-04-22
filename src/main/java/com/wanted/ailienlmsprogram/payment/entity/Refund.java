package com.wanted.ailienlmsprogram.payment.entity;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "REFUND")
@Getter
@NoArgsConstructor
public class Refund {

    public enum RefundStatus {
        REQUESTED, APPROVED, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id")
    private Long refundId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "refund_reason", columnDefinition = "TEXT")
    private String refundReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_status", nullable = false, length = 20)
    private RefundStatus refundStatus;

    @Column(name = "refund_requested_at")
    private LocalDateTime refundRequestedAt;

    @Column(name = "refund_processed_at")
    private LocalDateTime refundProcessedAt;

    // 생성 메서드
    public static Refund create(Payment payment, Member member, String refundReason) {
        Refund refund = new Refund();
        refund.payment = payment;
        refund.member = member;
        refund.refundReason = refundReason;
        refund.refundStatus = RefundStatus.REQUESTED;
        refund.refundRequestedAt = LocalDateTime.now();
        return refund;
    }

    // 도메인 메서드
    public void approve() {
        this.refundStatus = RefundStatus.APPROVED;
        this.refundProcessedAt = LocalDateTime.now();
    }

    public void reject() {
        this.refundStatus = RefundStatus.REJECTED;
        this.refundProcessedAt = LocalDateTime.now();
    }
}
