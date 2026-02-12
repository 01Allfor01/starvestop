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
public class GetStoreForOwnerResponse {
    private final Long id;
    private final String name;
    private final String address;
    private final StoreCategory category;
    private final Double latitude;
    private final Double longitude;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final StoreStatus status;
    private final Long unreadCount;
    private final String ImageUrl;

    public static GetStoreForOwnerResponse from(Store store, Long unreadCount) {
        Point point = store.getLocation();
        return new GetStoreForOwnerResponse(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getCategory(),
                point.getY(),
                point.getX(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getStatus(),
                unreadCount,
                store.getImageUuid()
        );
    }
}
