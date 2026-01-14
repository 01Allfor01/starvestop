package com.allforone.starvestop.domain.subscription.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreateSubscriptionRequest {

    @NotBlank
    private Long storeId;

    @NotBlank
    private String subscriptionName;

    @NotBlank
    private BigDecimal price;
}
