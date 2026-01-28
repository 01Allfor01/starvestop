package com.allforone.starvestop.domain.store.dto.request;

import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class CreateStoreRequest {

    @NotNull(message = "판매자 아이디를 적어주세요")
    private Long ownerId;

    @Size(max = 255, message = "매장 이름은 255자 이하로 입력해주세요")
    @NotBlank(message = "매장 이름을 적어주세요")
    private String name;

    @Size(max = 255, message = "매장 주소는 255자 이하로 입력해주세요")
    @NotBlank(message = "주소를 적어주세요")
    private String address;

    @Size(max = 255, message = "매장 설명은 255자 이하로 입력해주세요")
    @NotNull(message = "매장 설명을 적어주세요")
    private String description;

    @NotNull(message = "매장 카테고리를 선택해주세요")
    private StoreCategory category;

    @NotNull(message = "매장 위치를 입력해주세요")
    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 90 이하여야 합니다")
    private Double latitude;

    @NotNull(message = "매장 위치를 입력해주세요")
    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 180 이하여야 합니다")
    private Double longitude;

    @NotNull(message = "매장 오픈 시간을 입력해주세요")
    private LocalTime openTime;

    @NotNull(message = "매장 마감 시간을 입력해주세요")
    private LocalTime closeTime;

    private StoreStatus status;

    @Size(max = 255, message = "사업자 등록 번호는 255자 이하로 입력해주세요")
    @NotBlank(message = "사업자 등록 번호를 입력해주세요")
    private String businessRegistrationNumber;
}
