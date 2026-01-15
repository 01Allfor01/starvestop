package com.allforone.starvestop.domain.product.dto.response;

import com.allforone.starvestop.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetProductSaleResponse {
    private final String productName;
    private final BigDecimal price;
    private final BigDecimal salePrice;

    public static GetProductSaleResponse from(Product product) {
        return new GetProductSaleResponse(
                product.getProductName(),
                product.getPrice(),
                product.getSalePrice()
        );
    }
}
