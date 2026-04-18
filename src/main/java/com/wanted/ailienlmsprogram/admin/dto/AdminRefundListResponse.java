package com.wanted.ailienlmsprogram.admin.dto;

import com.wanted.ailienlmsprogram.payment.entity.Payment;
import com.wanted.ailienlmsprogram.payment.entity.Refund;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/*관리자 환불 목록 화면에 출력할 데이터를 담는 DTO.
*
* - 환불 엔티티와 결제/회원 정보를 조합해
* - 관리자 환불 목록 화면에 필요한 값으로 가공한다.*/
@Getter
@AllArgsConstructor
public class AdminRefundListResponse {

    private Long refundId;
    private String paymentOrderNo;
    private Integer paymentTotalPrice;

    private Long memberId;
    private String memberLoginId;
    private String memberName;

    private String refundReason;
    private Refund.RefundStatus refundStatus;
    private LocalDateTime refundRequestedAt;
    private LocalDateTime refundProcessedAt;

    //Refund 엔티티를 관리자 환불 목록 DTO로 변환한다.
    public static AdminRefundListResponse from(Refund refund) {
        Payment payment = refund.getPayment();

        return new AdminRefundListResponse(
                refund.getRefundId(),
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

    //현재 환불 요청이 처리 대기 상태인지 여부를 반환한다.
    public boolean isRequested() {
        return refundStatus == Refund.RefundStatus.REQUESTED;
    }

    // 환불 상태를 화면 표시용 문자열로 변환한다.
    public String getDisplayStatus() {
        return switch (refundStatus) {
            case REQUESTED -> "처리 대기";
            case APPROVED -> "승인 완료";
            case REJECTED -> "거절 완료";
        };
    }
}