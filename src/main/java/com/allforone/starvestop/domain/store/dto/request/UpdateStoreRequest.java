package com.allforone.starvestop.domain.store.dto.request;

import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Schema(description = "매장 수정 요청")
public class UpdateStoreRequest {

    @Schema(description = "매장 이름", example = "판교 수육 국밥집")
    @Size(max = 255, message = "매장 이름은 255자 이하로 입력해주세요")
    @NotBlank(message = "매장 이름을 적어주세요")
    private String name;

    @Schema(description = "매장 주소", example = "경기도 성남시 분당구 대왕판교로 256")
    @Size(max = 255, message = "매장 주소는 255자 이하로 입력해주세요")
    @NotBlank(message = "주소를 적어주세요")
    private String address;

    @Schema(description = "매장 설명", example = "수육과 국밥 전문")
    @Size(max = 255, message = "매장 설명은 255자 이하로 입력해주세요")
    @NotBlank(message = "매장 설명을 적어주세요")
    private String description;

    @Schema(description = "매장 카테고리", example = "KOREAN_FOOD")
    @NotNull(message = "매장 카테고리를 선택해주세요")
    private StoreCategory category;

    @Schema(description = "위도", example = "37.398902")
    @NotNull(message = "매장 위치를 입력해주세요")
    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 90 이하여야 합니다")
    private Double latitude;

    @Schema(description = "경도", example = "127.115672")
    @NotNull(message = "매장 위치를 입력해주세요")
    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 180 이하여야 합니다")
    private Double longitude;

    @Schema(description = "오픈 시간", example = "09:00:00")
    @NotNull(message = "매장 오픈 시간을 입력해주세요")
    private LocalTime openTime;

    @Schema(description = "마감 시간", example = "21:00:00")
    @NotNull(message = "매장 마감 시간을 입력해주세요")
    private LocalTime closeTime;

    @Schema(description = "매장 상태", example = "OPEN")
    private StoreStatus status;
}
