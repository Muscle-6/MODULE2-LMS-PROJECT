package com.wanted.ailienlmsprogram.payment.client;

import com.wanted.ailienlmsprogram.payment.dto.TossPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * 토스페이먼츠 REST API 클라이언트.
 * secretKey + ":" 를 Base64 인코딩한 값을 Authorization 헤더에 사용한다.
 */
@Slf4j
@Component
public class TossPaymentClient {

    @Value("${toss.secret-key}")
    private String secretKey;

    private final RestClient restClient = RestClient.create();

    // ── 결제 확인 ────────────────────────────────────────────────────────────

    /**
     * POST /v1/payments/confirm
     * 결제 승인: 클라이언트 SDK가 반환한 paymentKey, orderId, amount로 서버 측 최종 승인.
     *
     * @return
     */
    public TossPaymentResponse confirm(String paymentKey, String orderId, long amount) {
        Map<String, Object> body = Map.of(
                "paymentKey", paymentKey,
                "orderId",    orderId,
                "amount",     amount
        );

        try {
            return restClient.post()
                    .uri("https://api.tosspayments.com/v1/payments/confirm")
                    .header("Authorization", basicAuth())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(TossPaymentResponse.class); // 토스 결과값

        } catch (RestClientException e) {
            log.error("Toss confirm failed: paymentKey={}, orderId={}", paymentKey, orderId, e);
            throw new IllegalArgumentException("토스페이먼츠 결제 확인에 실패했습니다.");
        }
    }

    // ── 결제 취소(환불) ──────────────────────────────────────────────────────

    /**
     * POST /v1/payments/{paymentKey}/cancel
     * 전체 취소: cancelReason이 null이면 기본 문구를 사용한다.
     */
    public void cancel(String paymentKey, String cancelReason) {
        String reason = (cancelReason != null && !cancelReason.isBlank())
                ? cancelReason
                : "고객 요청에 의한 취소";

        Map<String, Object> body = Map.of("cancelReason", reason);

        try {
            restClient.post()
                    .uri("https://api.tosspayments.com/v1/payments/{paymentKey}/cancel", paymentKey)
                    .header("Authorization", basicAuth())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException e) {
            String tossError = e.getResponseBodyAsString();
            // 더미 데이터 또는 만료된 테스트 키처럼 Toss에 존재하지 않는 결제는 취소할 것이 없으므로 그냥 통과
            if (tossError != null && tossError.contains("NOT_FOUND_PAYMENT")) {
                log.warn("Toss cancel skipped (payment not found in Toss): paymentKey={}", paymentKey);
                return;
            }
            log.error("Toss cancel failed: paymentKey={}, status={}, body={}", paymentKey, e.getStatusCode(), tossError);
            throw new IllegalArgumentException("토스페이먼츠 결제 취소에 실패했습니다. (Toss 응답: " + tossError + ")");
        } catch (RestClientException e) {
            log.error("Toss cancel failed (network): paymentKey={}", paymentKey, e);
            throw new IllegalArgumentException("토스페이먼츠 결제 취소에 실패했습니다.");
        }
    }

    // ── 공통 ─────────────────────────────────────────────────────────────────

    /** secretKey + ":" → Base64 → "Basic ..." 헤더 값 생성 */
    private String basicAuth() {
        return "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
    }

}
