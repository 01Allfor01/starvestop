package com.allforone.starvestop.domain.product.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class UpdateProductRequest {
    private String productName;
    private String description;
    private BigDecimal price;
    private BigDecimal salePrice;
    @Pattern(regexp = "GENERAL|SALE", message = "상품 상태는 GENERAL, SALE만 가능합니다")
    private String status;
}
