package com.allforone.starvestop.domain.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "결제 생성 요청")
@Getter
@NoArgsConstructor
public class CreatePaymentRequest {
    @Schema(description = "주문 ID", example = "100")
    @NotNull
    private Long orderId;
}
