package com.allforone.starvestop.domain.product.dto.request;

import com.allforone.starvestop.domain.product.enums.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "상품 수정 요청")
@Getter
@NoArgsConstructor
@ValidSalePrice
public class UpdateProductRequest implements PriceComparableRequest {
    @Schema(description = "상품명", example = "프리미엄 국밥")
    @Size(max = 255, message = "상품 이름을 255자 이하로 입력해주세요")
    @NotBlank(message = "상품 이름을 적어주세요")
    private String name;

    @Schema(description = "상품 설명", example = "진하게 우려낸 사골육수")
    @Size(max = 255, message = "상품 설명을 255자 이하로 입력해주세요")
    @NotBlank(message = "상품 설명을 적어주세요")
    private String description;

    @Schema(description = "재고", example = "30")
    @Max(value = 10000, message = "상품 재고를 10000이하로 입력해주세요")
    @NotNull(message = "상품 재고를 적어주세요")
    private Integer stock;

    @Schema(description = "정가", example = "6500")
    @NotNull(message = "상품 가격을 적어주세요")
    @DecimalMin(value = "0.0", message = "상품 가격은 0 이상이어야 합니다")
    @DecimalMax(value = "1000000", message = "상품 가격은 1000000원 이하여야 합니다")
    private BigDecimal price;

    @Schema(description = "세일 가격", example = "5200")
    @NotNull(message = "상품 세일 가격을 적어주세요")
    @DecimalMin(value = "0.0", message = "상품 세일 가격은 0 이상이어야 합니다")
    @DecimalMax(value = "1000000", message = "상품 가격은 1000000원 이하여야 합니다")
    private BigDecimal salePrice;

    @Schema(description = "상품 상태", example = "SALE")
    @NotNull(message = "상품 상태를 적어주세요")
    private ProductStatus status;
}
