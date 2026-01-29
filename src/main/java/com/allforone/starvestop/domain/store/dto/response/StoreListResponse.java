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
    Long id;
    String name;
    String address;
    StoreCategory category;
    Point location;
    LocalTime openTime;
    LocalTime closeTime;
    StoreStatus status;

    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getCategory(),
                store.getLocation(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getStatus()
        );
    }
}
