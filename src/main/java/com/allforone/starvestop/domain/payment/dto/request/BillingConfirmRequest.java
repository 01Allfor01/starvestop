package com.allforone.starvestop.domain.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "빌링 승인 요청")
@Getter
@NoArgsConstructor
public class BillingConfirmRequest {
    @Schema(example = "customer_123")
    @NotBlank
    private String customerKey;
    @Schema(example = "auth_abc")
    @NotBlank
    private String authKey;
    @Schema(description = "구독 ID", example = "10")
    @NotNull
    private Long subscriptionId;
}
