package com.allforone.starvestop.domain.product.dto.response;

import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UpdateProductResponse {
    private final Long productId;
    private final Long storeId;
    private final String productName;
    private final String description;
    private final BigDecimal price;
    private final BigDecimal salePrice;
    private final ProductStatus status;

    public static UpdateProductResponse from(Product product) {
        return new UpdateProductResponse(
                product.getId(),
                product.getStore().getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getSalePrice(),
                product.getStatus()
        );
    }
}
