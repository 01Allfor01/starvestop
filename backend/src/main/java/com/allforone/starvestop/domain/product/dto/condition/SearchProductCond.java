package com.allforone.starvestop.domain.product.dto.condition;

import com.allforone.starvestop.domain.store.enums.StoreCategory;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchProductCond {
    private final String keyword;
    private final StoreCategory category;

    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 90 이하여야 합니다")
    private final Double nowLatitude;

    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 180 이하여야 합니다")
    private final Double nowLongitude;

    private final Integer size;
    private final Double lastDistance;
    private final Long lastId;
    private final Long lastStoreId;
}