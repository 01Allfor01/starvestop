package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final @Qualifier("paymentWebClient") WebClient paymentWebClient;

    public Payment getByOrderKey(String orderKey) {
        return paymentRepository.getPaymentByOrderKey(orderKey);
    }

    public Optional<Payment> findByOrderKey(String orderKey) {
        return paymentRepository.findPaymentByOrderKey(orderKey);
    }

    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    public String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return "{\"error\":\"payload_serialization_failed\"}";
        }
    }

    public Payment findByOrderKeyForUpdate(String orderKey) {
        return paymentRepository.findByOrderKeyForUpdate(orderKey)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    public List<Payment> findByOrderUserId(Long userId) {
        return paymentRepository.findAllByOrderUserIdAndIsDeletedIsFalseOrderByCreatedAtDesc(userId);
    }

    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }


    public int checkClaimed(Payment payment) {
        return paymentRepository.markFailedAndClaimStockRelease(
                payment.getId(),
                PaymentStatus.FAILED,
                List.of(PaymentStatus.REQUESTED, PaymentStatus.PENDING)
        );
    }

    @Nullable
    public Map tossApiConfirm(Map<String, Object> requestPayload) {
        Map response = paymentWebClient.post()
                .uri("/v1/payments/confirm")
                .bodyValue(requestPayload)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        return response;
    }

}
