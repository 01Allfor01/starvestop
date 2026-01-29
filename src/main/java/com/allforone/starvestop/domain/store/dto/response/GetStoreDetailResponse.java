package com.allforone.starvestop.domain.store.dto.response;

import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class GetStoreDetailResponse {
    private final Long id;
    private final String name;
    private final String address;
    private final String description;
    private final StoreCategory category;
    private final Point location;
    private final StoreStatus status;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final String businessRegistrationNumber;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static GetStoreDetailResponse from(Store store, String imageUrl) {
        return new GetStoreDetailResponse(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getDescription(),
                store.getCategory(),
                store.getLocation(),
                store.getStatus(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getBusinessRegistrationNumber(),
                imageUrl,
                store.getCreatedAt(),
                store.getUpdatedAt()
        );
    }

}
