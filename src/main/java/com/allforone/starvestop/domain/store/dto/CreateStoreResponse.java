package com.allforone.starvestop.domain.store.dto;

import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CreateStoreResponse {
    private final Long storeId;
    private final String storeName;
    private final String address;
    private final String description;
    private final StoreCategory category;
    private final StoreStatus status;
    private final LocalDateTime createdAt;

    public static CreateStoreResponse from(Store store) {
        return new CreateStoreResponse(
                store.getId(),
                store.getStoreName(),
                store.getAddress(),
                store.getDescription(),
                store.getCategory(),
                store.getStatus(),
                store.getCreatedAt()
        );
    }
}
