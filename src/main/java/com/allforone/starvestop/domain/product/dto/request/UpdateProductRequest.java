package com.allforone.starvestop.domain.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class UpdateProductRequest {
    @NotBlank(message = "상품 이름을 적어주세요")
    private String productName;
    @NotBlank(message = "상품 설명을 적어주세요")
    private String description;
    @NotNull(message = "상품 재고를 적어주세요")
    private Long stock;
    @NotNull(message = "상품 가격을 적어주세요")
    @DecimalMin(value = "0.0", inclusive = true, message = "상품 가격은 0 이상이어야 합니다")
    private BigDecimal price;
    @NotNull(message = "상품 세일 가격을 적어주세요")
    @DecimalMin(value = "0.0", inclusive = true, message = "상품 세일 가격은 0 이상이어야 합니다")
    private BigDecimal salePrice;
    @NotBlank(message = "상품 상태를 적어주세요")
    @Pattern(regexp = "GENERAL|SALE", message = "상품 상태는 GENERAL, SALE만 가능합니다")
    private String status;
}
