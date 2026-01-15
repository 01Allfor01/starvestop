package com.allforone.starvestop.domain.product.dto.response;

import com.allforone.starvestop.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetProductResponse {
    private final String productName;
    private final String description;
    private final BigDecimal price;
    private final BigDecimal salePrice;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static GetProductResponse from(Product product) {
        return new GetProductResponse(
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getSalePrice(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
