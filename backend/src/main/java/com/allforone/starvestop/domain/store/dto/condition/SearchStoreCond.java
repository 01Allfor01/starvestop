package com.allforone.starvestop.domain.store.dto.condition;

import com.allforone.starvestop.domain.store.enums.StoreCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "매장 검색 조건 (Query Params)")
public class SearchStoreCond {

    @Schema(description = "검색 키워드", example = "국밥")
    private final String keyword;
    @Schema(description = "카테고리", example = "KOREAN_FOOD")
    private final StoreCategory category;

    @Schema(description = "현재 위도", example = "37.392115")
    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 90 이하여야 합니다")
    private final Double nowLatitude;

    @Schema(description = "현재 경도", example = "127.108542")
    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 180 이하여야 합니다")
    private final Double nowLongitude;

    @Schema(description = "페이지 크기", example = "10")
    private final Integer size;
    @Schema(description = "마지막 거리 (무한스크롤용)", example = "1.234")
    private final Double lastDistance;
    @Schema(description = "마지막 매장 ID (무한스크롤용)", example = "100")
    private final Long lastId;
}
