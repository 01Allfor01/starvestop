package com.allforone.starvestop.domain.product.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

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
        LocalTime closeTime,
        java.time.LocalDateTime updatedAt) {
}
