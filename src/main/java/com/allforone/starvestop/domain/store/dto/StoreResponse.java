package com.allforone.starvestop.domain.store.dto;

import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class StoreResponse {
    private final Long storeId;
    private final String storeName;
    private final String address;
    private final String description;
    private final StoreCategory category;
    private final Point location;
    private final StoreStatus status;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getStoreName(),
                store.getAddress(),
                store.getDescription(),
                store.getCategory(),
                store.getLocation(),
                store.getStatus(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getCreatedAt(),
                store.getUpdatedAt()
        );
    }
}
