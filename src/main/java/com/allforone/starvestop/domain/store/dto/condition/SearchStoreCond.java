package com.allforone.starvestop.domain.store.dto.condition;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;

@Getter
public class SearchStoreCond {

    private final String keyword;
    private final String category;

    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 90 이하여야 합니다")
    private final Double nowLatitude;

    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 180 이하여야 합니다")
    private final Double nowLongitude;

    private final int page;
    private final int size;

    public SearchStoreCond(String keyword,
                           String category,
                           Double nowLatitude,
                           Double nowLongitude,
                           Integer page,
                           Integer size) {
        this.keyword = keyword;
        this.category = category;
        this.nowLatitude = nowLatitude;
        this.nowLongitude = nowLongitude;
        this.page = page != null ? page : 0;
        this.size = size != null ? size : 10;
    }
}
