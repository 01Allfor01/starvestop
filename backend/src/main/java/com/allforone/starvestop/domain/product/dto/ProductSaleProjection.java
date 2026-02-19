package com.allforone.starvestop.domain.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface ProductSaleProjection {
    Long getId();

    Long getStoreId();

    String getStoreName();

    String getName();

    String getDescription();

    Integer getStock();

    BigDecimal getPrice();

    BigDecimal getSalePrice();

    String getImageUuid();

    LocalTime getCloseTime();

    LocalDateTime getUpdatedAt();
}