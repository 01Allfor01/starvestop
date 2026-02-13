package com.allforone.starvestop.domain.payment.dto.response;

import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.entity.PaymentLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetPaymentLogDetailResponse {
    private final Long paymentId;
    private final Long userId;
    private final String orderKey;
    private final PaymentStatus paymentStatus;
    private String payload;
    private LocalDateTime timestamp;

    public static GetPaymentLogDetailResponse from(PaymentLog paymentLog
    ) {
        return new GetPaymentLogDetailResponse(
                paymentLog.getPaymentId(),
                paymentLog.getUserId(),
                paymentLog.getOrderKey(),
                paymentLog.getPaymentStatus(),
                paymentLog.getPayload(),
                paymentLog.getTimestamp()
        );
    }
}
