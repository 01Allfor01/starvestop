package com.allforone.starvestop.domain.product.dto.response;

import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.geo.Point;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetProductDetailResponse {
    private final Long productId;
    private final Long storeId;
    private final String storeName;
    private final Point location;
    private final String productName;
    private final String description;
    private final Long stock;
    private final BigDecimal price;
    private final BigDecimal salePrice;
    private final ProductStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static GetProductDetailResponse from(Product product) {
        return new GetProductDetailResponse(
                product.getId(),
                product.getStore().getId(),
                product.getStore().getStoreName(),
                product.getStore().getLocation(),
                product.getProductName(),
                product.getDescription(),
                product.getStock(),
                product.getPrice(),
                product.getSalePrice(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
