package com.allforone.starvestop.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CreateProductRequest {

    @NotNull(message = "매장 아이디를 적어주세요")
    private final Long storeId;
    @NotBlank(message = "상품 이름을 적어주세요")
    private final String productName;
    @NotBlank(message = "상품 설명을 적어주세요")
    private final String description;
    @NotNull(message = "상품 가격을 적어주세요")
    private final BigDecimal price;
    private final BigDecimal salePrice;
    @NotBlank(message = "상품 상태를 적어주세요")
    @Pattern(regexp = "GENERAL|SALE", message = "상품 상태는 GENERAL, SALE만 가능합니다")
    private final String status;
}
