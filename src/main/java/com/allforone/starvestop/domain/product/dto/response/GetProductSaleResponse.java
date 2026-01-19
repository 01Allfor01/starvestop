package com.allforone.starvestop.domain.product.dto.response;

import com.allforone.starvestop.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetProductSaleResponse {
    private final Long productId;
    private final Long storedId;
    private final String storeName;
    private final String productName;
    private final String description;
    private final Long stock;
    private final BigDecimal price;
    private final BigDecimal salePrice;

    public static GetProductSaleResponse from(Product product) {
        return new GetProductSaleResponse(
                product.getId(),
                product.getStore().getId(),
                product.getStore().getStoreName(),
                product.getProductName(),
                product.getDescription(),
                product.getStock(),
                product.getPrice(),
                product.getSalePrice()
        );
    }
}
