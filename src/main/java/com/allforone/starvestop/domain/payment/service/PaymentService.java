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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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


    @Transactional
    public Payment saveAndFlush(Payment payment) {
        return paymentRepository.saveAndFlush(payment);
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

    public Page<Payment> findByOrderUserId(Long userId, Pageable pageable) {
        return paymentRepository.findAllByOrderUserId(userId, pageable);
    }

    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    public int checkClaimed(Payment payment) {
        return paymentRepository.markClaimStockRelease(
                payment.getId(),
                List.of(
                        PaymentStatus.REQUESTED,
                        PaymentStatus.PENDING,
                        PaymentStatus.FAILED_NON_RETRYABLE
                )
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
