package com.allforone.starvestop.domain.product.dto.request;

import com.allforone.starvestop.domain.product.enums.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreateProductRequest {
    @NotNull(message = "매장 아이디를 적어주세요")
    private Long storeId;

    @Size(max = 255, message = "상품 이름을 255자 이하로 입력해주세요")
    @NotBlank(message = "상품 이름을 적어주세요")
    private String productName;

    @Size(max = 255, message = "상품 설명을 255자 이하로 입력해주세요")
    @NotBlank(message = "상품 설명을 적어주세요")
    private String description;

    @Max(value = 10000, message = "상품 재고를 10000이하로 입력해주세요")
    @NotNull(message = "상품 재고를 적어주세요")
    private Long stock;

    @NotNull(message = "상품 가격을 적어주세요")
    @DecimalMin(value = "0.0", inclusive = true, message = "상품 가격은 0 이상이어야 합니다")
    private BigDecimal price;

    @NotNull(message = "상품 세일 가격을 적어주세요")
    @DecimalMin(value = "0.0", inclusive = true, message = "상품 세일 가격은 0 이상이어야 합니다")
    private BigDecimal salePrice;

    @NotNull(message = "상품 상태를 적어주세요")
    private ProductStatus status;
}
