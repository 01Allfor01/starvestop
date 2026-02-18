package com.allforone.starvestop.domain.product.dto.response;

import com.allforone.starvestop.domain.product.dto.ProductSaleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private final LocalTime endTime;
    private final Double distance;
    private final LocalDateTime updatedAt;

    public static GetProductSaleResponse from(ProductSaleDto productSaleDto, Double distance, String imageUrl) {
        return new GetProductSaleResponse(
                productSaleDto.id(),
                productSaleDto.storeId(),
                productSaleDto.storeName(),
                productSaleDto.name(),
                productSaleDto.description(),
                productSaleDto.stock(),
                productSaleDto.price(),
                productSaleDto.salePrice(),
                imageUrl,
                distance,
                productSaleDto.updatedAt().toLocalDateTime()
        );
    }
}
