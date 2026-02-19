package com.allforone.starvestop.domain.cart.dto;

import com.allforone.starvestop.domain.cart.entity.Cart;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "장바구니 응답")
@Getter
@AllArgsConstructor
public class CartResponse {
    @Schema(example = "10")
    private Long id;
    @Schema(example = "100")
    private Long productId;
    @Schema(example = "프리미엄 국밥")
    private String productName;
    @Schema(example = "2")
    private Integer quantity;

    public CartResponse(Cart cart) {
        this.id = cart.getId();
        this.productId = cart.getProduct().getId();
        this.productName = cart.getProduct().getName();
        this.quantity = cart.getQuantity();
    }
}
