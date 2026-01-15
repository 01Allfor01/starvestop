package com.allforone.starvestop.domain.product.dto.response;

import com.allforone.starvestop.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetProductResponse {
    private final String productName;
    private final BigDecimal price;

    public static GetProductResponse from(Product product) {
        return new GetProductResponse(
                product.getProductName(),
                product.getPrice()
        );
    }
}
