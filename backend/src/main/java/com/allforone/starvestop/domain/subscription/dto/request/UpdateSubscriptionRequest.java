package com.allforone.starvestop.domain.subscription.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "구독 수정 요청")
public class UpdateSubscriptionRequest {
    @Schema(description = "가입 가능 여부", example = "true")
    @NotNull(message = "구독 가입 가능 여부를 선택해주세요")
    private Boolean joinable;
}
