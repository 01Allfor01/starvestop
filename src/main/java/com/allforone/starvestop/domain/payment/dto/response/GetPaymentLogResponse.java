package com.allforone.starvestop.domain.payment.dto.response;

import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.entity.PaymentLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetPaymentLogResponse {
    private Long id;
    private String orderKey;
    private PaymentStatus status;
    private LocalDateTime timestamp;

    public static GetPaymentLogResponse from(PaymentLog paymentLog) {
        return new GetPaymentLogResponse(paymentLog.getId(), paymentLog.getOrderKey(), paymentLog.getPaymentStatus(), paymentLog.getTimestamp());
    }
}
