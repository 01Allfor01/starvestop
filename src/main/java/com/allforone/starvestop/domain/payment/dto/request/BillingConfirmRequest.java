package com.allforone.starvestop.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BillingConfirmRequest {
    @NotBlank
    private String customerKey;
    @NotBlank
    private String authKey;
    @NotNull
    private Long subscriptionId;
}
