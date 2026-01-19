package com.allforone.starvestop.domain.store.dto.condition;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchStoreCond {

    private String storeName;
    private String category;
    private String productName;

    @Min(value = 1)
    @Max(value = 127)
    private Integer day;

    @Min(value = 1)
    @Max(value = 7)
    private Integer mealTime;

    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 90 이하여야 합니다")
    private Double nowLatitude;

    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 180 이하여야 합니다")
    private Double nowLongitude;
}
