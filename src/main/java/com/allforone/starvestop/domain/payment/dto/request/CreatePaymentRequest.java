package com.allforone.starvestop.domain.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreatePaymentRequest {
    private Long productId;
    private Long userSubscriptionId;
    @NotNull
    private BigDecimal amount;
}
