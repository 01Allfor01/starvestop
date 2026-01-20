package com.allforone.starvestop.domain.store.dto.response;

import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreListResponse {
    Long storeId;
    String storeName;
    String address;
    StoreCategory category;
    Point location;
    LocalTime openTime;
    LocalTime closeTime;
    StoreStatus status;

    public static StoreListResponse from(Store store) {
        return new StoreListResponse(
                store.getId(),
                store.getStoreName(),
                store.getAddress(),
                store.getCategory(),
                store.getLocation(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getStatus()
        );
    }
}
