package com.allforone.starvestop.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {
    @NotNull(message = "매장 아이디를 입력해주세요")
    private Long storeId;
    private Long userCouponId;
}
