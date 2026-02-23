package com.allforone.starvestop.domain.payment.dto.response;

import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetPaymentResponse {

    private final Long paymentId;
    private final String orderKey;
    private final PaymentStatus paymentStatus;
    private final BigDecimal amount;

    public static GetPaymentResponse from(Payment payment) {

        return new GetPaymentResponse(
                payment.getId(),
                payment.getOrderKey(),
                payment.getStatus(),
                payment.getAmount()
        );
    }
}
