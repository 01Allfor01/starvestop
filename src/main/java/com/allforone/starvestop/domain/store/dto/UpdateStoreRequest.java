package com.allforone.starvestop.domain.store.dto;

import com.allforone.starvestop.domain.store.enums.StoreCategory;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class UpdateStoreRequest {
    private String storeName;
    private String address;
    private String description;
    private StoreCategory category;
    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 90 이하여야 합니다")
    private Double latitude;
    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 180 이하여야 합니다")
    private Double longitude;
    private LocalTime openTime;
    private LocalTime closeTime;
}
