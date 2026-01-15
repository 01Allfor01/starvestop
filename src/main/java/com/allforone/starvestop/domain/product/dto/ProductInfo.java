package com.allforone.starvestop.domain.product.dto;

import com.allforone.starvestop.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductInfo {
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final BigDecimal salePrice;

    public static ProductInfo from(Product product) {
        return new ProductInfo(
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getSalePrice()
        );
    }
}