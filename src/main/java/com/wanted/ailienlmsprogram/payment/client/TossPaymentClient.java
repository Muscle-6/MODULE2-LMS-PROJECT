package com.wanted.ailienlmsprogram.payment.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * 토스페이먼츠 결제 확인 API 클라이언트.
 * POST https://api.tosspayments.com/v1/payments/confirm
 */
@Slf4j
@Component
public class TossPaymentClient {

    @Value("${toss.secret-key}")
    private String secretKey;

    private final RestClient restClient = RestClient.create();

    public void confirm(String paymentKey, String orderId, long amount) {
        String authorization = "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        Map<String, Object> body = Map.of(
                "paymentKey", paymentKey,
                "orderId", orderId,
                "amount", amount
        );

        try {
            restClient.post()
                    .uri("https://api.tosspayments.com/v1/payments/confirm")
                    .header("Authorization", authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            log.error("Toss confirm failed: paymentKey={}, orderId={}", paymentKey, orderId, e);
            throw new IllegalArgumentException("토스페이먼츠 결제 확인에 실패했습니다.");
        }
    }
}
