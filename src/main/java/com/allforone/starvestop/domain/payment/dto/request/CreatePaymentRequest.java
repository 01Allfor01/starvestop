package com.allforone.starvestop.domain.payment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreatePaymentRequest {
    private Long productId;
    private Long userSubscriptionId;
    private BigDecimal amount;
}
