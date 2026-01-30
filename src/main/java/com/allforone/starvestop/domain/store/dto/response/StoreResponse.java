package com.allforone.starvestop.domain.store.dto.response;

import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreResponse {
    private final Long id;
    private final String name;
    private final String address;
    private final StoreCategory category;
    private final Double latitude;
    private final Double longitude;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final StoreStatus status;
    private final String ImageUrl;

    public static StoreResponse from(StoreDto storeDto, String imageUrl) {
        Point point = storeDto.getLocation();
        return new StoreResponse(
                storeDto.getId(),
                storeDto.getName(),
                storeDto.getAddress(),
                storeDto.getCategory(),
                point.getY(),
                point.getX(),
                storeDto.getOpenTime(),
                storeDto.getCloseTime(),
                storeDto.getStatus(),
                imageUrl
        );
    }
}
