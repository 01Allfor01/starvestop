package com.allforone.starvestop.domain.subscription.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class UpdateSubscriptionRequest {

    @NotBlank(message = "수정 할 구독 이름을 적어주세요")
    private String subscriptionName;

    @NotBlank(message = "수정 할 구독 설명을 적어주세요")
    private String description;

    @NotBlank(message = "수정 할 가격을 적어주세요")
    private BigDecimal price;
}
