package com.allforone.starvestop.domain.product.dto.response;

import com.allforone.starvestop.domain.product.dto.ProductSaleDto;
import com.allforone.starvestop.domain.store.dto.StoreRedisDto;
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
    private final Double distance;
    private final LocalDateTime updatedAt;

    public static GetProductSaleResponse from(ProductSaleDto productSaleDto, StoreRedisDto redisDto, String imageUrl) {
        return new GetProductSaleResponse(
                productSaleDto.getId(),
                productSaleDto.getStoreId(),
                productSaleDto.getStoreName(),
                productSaleDto.getName(),
                productSaleDto.getDescription(),
                productSaleDto.getStock(),
                productSaleDto.getPrice(),
                productSaleDto.getSalePrice(),
                imageUrl,
                redisDto.getDistance(),
                productSaleDto.getUpdatedAt()
        );
    }
}
