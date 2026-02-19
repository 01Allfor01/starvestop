package com.allforone.starvestop.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartListResponse {
    private final Long storeId;
    private final List<CartResponse> cartList;
}
