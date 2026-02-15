package com.allforone.starvestop.domain.product.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record ProductSaleDto(
        Long id,
        Long storeId,
        String storeName,
        String name,
        String description,
        Integer stock,
        BigDecimal price,
        BigDecimal salePrice,
        String imageUuid,
        Timestamp updatedAt) {
}
