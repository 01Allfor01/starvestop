package com.allforone.starvestop.domain.product.dto;

import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductDto {
    private final Long id;
    private final Long storeId;
    private final String storeName;
    private final Point location;
    private final String name;
    private final String description;
    private final Integer stock;
    private final BigDecimal price;
    private final BigDecimal salePrice;
    private final ProductStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProductDto from(Product product) {
        return new ProductDto(
                product.getId(),
                product.getStore().getId(),
                product.getStore().getName(),
                product.getStore().getLocation(),
                product.getName(),
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
