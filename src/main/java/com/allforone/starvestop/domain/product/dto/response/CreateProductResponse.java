package com.allforone.starvestop.domain.product.dto.response;

import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CreateProductResponse {
    private final Long productId;
    private final Long storeId;
    private final String storeName;
    private final String productName;
    private final String description;
    private final Long stock;
    private final BigDecimal price;
    private final BigDecimal salePrice;
    private final ProductStatus status;

    public static CreateProductResponse from(Product product) {
        return new CreateProductResponse(
                product.getId(),
                product.getStore().getId(),
                product.getStore().getStoreName(),
                product.getProductName(),
                product.getDescription(),
                product.getStock(),
                product.getPrice(),
                product.getSalePrice(),
                product.getStatus()
        );
    }

}
