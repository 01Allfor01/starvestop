package com.allforone.starvestop.domain.payment.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TossBillingClient {

    private final WebClient paymentWebClient;

    public Map issueBillingKey(String authKey, String customerKey) {
        return paymentWebClient.post()
                .uri("/v1/billing/authorizations/issue")
                .bodyValue(Map.of("authKey", authKey, "customerKey", customerKey))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map approveBilling(String billingKey, String customerKey, String orderId, long amount, String orderName) {
        return paymentWebClient.post()
                .uri("/v1/billing/{billingKey}", billingKey)
                .bodyValue(Map.of(
                        "customerKey", customerKey,
                        "orderId", orderId,
                        "orderName", orderName,
                        "amount", amount
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
