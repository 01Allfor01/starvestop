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

    private final int size;
    private final Long cursorId;

    public SearchStoreCond(String keyword,
                           String category,
                           Double nowLatitude,
                           Double nowLongitude,
                           Integer size,
                           Long cursorId) {
        this.keyword = keyword;
        this.category = category;
        this.nowLatitude = nowLatitude;
        this.nowLongitude = nowLongitude;
        this.size = size != null ? size : 10;
        this.cursorId = cursorId;
    }
}
