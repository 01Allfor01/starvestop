package com.allforone.starvestop.domain.cart.dto;

import com.allforone.starvestop.domain.cart.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartResponse {
    private Long cartId;
    private String productName;
    private Integer quantity;

    public CartResponse(Cart cart) {
        this.cartId = cart.getId();
        this.productName = cart.getProduct().getName();
        this.quantity = cart.getQuantity();
    }
}
