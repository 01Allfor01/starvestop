package com.allforone.starvestop.domain.product.dto.response;

import com.allforone.starvestop.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetProductSaleResponse {
    private final Long id;
    private final Long storeId;
    private final String storeName;
    private final String name;
    private final String description;
    private final Integer stock;
    private final BigDecimal price;
    private final BigDecimal salePrice;
    private final String imageUrl;
    private final LocalDateTime updatedAt;

    public static GetProductSaleResponse from(Product product, String imageUrl) {
        return new GetProductSaleResponse(
                product.getId(),
                product.getStore().getId(),
                product.getStore().getName(),
                product.getName(),
                product.getDescription(),
                product.getStock(),
                product.getPrice(),
                product.getSalePrice(),
                imageUrl,
                product.getUpdatedAt()
        );
    }
}
