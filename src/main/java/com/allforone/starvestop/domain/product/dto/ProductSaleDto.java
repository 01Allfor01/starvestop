package com.allforone.starvestop.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductSaleDto {
    private final Long id;
    private final Long storeId;
    private final String storeName;
    private final String name;
    private final String description;
    private final Integer stock;
    private final BigDecimal price;
    private final BigDecimal salePrice;
    private final String imageUuid;
    private final LocalDateTime updatedAt;
}
