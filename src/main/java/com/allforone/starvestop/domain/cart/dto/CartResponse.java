package com.allforone.starvestop.domain.cart.dto;

import com.allforone.starvestop.domain.cart.entity.Cart;
import com.allforone.starvestop.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private String userName;
    private String productName;
    private Integer quantity;

    public CartResponse(Cart cart) {
        this.id = cart.getId();
        this.userName = cart.getUser().getUsername();
        this.productName = cart.getProduct().getName();
        this.quantity = cart.getQuantity();
    }
}
