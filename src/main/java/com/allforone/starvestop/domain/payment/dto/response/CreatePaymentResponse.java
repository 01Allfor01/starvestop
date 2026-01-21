package com.allforone.starvestop.domain.payment.dto.response;

import com.allforone.starvestop.domain.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CreatePaymentResponse {
    private final String orderKey;
    private final BigDecimal total_amount;

    public static CreatePaymentResponse from(Payment payment) {
        return new CreatePaymentResponse(payment.getOrderKey(), payment.getTotalAmount());
    }
}
