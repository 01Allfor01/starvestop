package com.allforone.starvestop.domain.subscription.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateSubscriptionRequest {

    @NotBlank(message = "구독 가입 가능 여부를 선택해주세요")
    private boolean isJoinable;
}
