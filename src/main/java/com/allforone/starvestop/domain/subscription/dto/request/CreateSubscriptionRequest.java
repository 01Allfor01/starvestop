package com.allforone.starvestop.domain.subscription.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreateSubscriptionRequest {

    @NotNull(message = "매장 Id를 적어주세요 ")
    private Long storeId;

    @NotBlank(message = "구독 이름을 적어주세요")
    private String subscriptionName;

    @NotBlank(message = "구독 설명을 적어주세요")
    private String description;

    @NotNull(message = "가격을 적어주세요")
    private BigDecimal price;
}
