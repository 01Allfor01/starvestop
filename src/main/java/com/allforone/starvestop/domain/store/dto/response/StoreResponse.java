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
public class StoreResponse {
    Long id;
    String name;
    String address;
    StoreCategory category;
    Point location;
    LocalTime openTime;
    LocalTime closeTime;
    StoreStatus status;
    String ImageUrl;

    public static StoreResponse from(StoreDto storeDto, String imageUrl) {
        return new StoreResponse(
                storeDto.getId(),
                storeDto.getName(),
                storeDto.getAddress(),
                storeDto.getCategory(),
                storeDto.getLocation(),
                storeDto.getOpenTime(),
                storeDto.getCloseTime(),
                storeDto.getStatus(),
                imageUrl
        );
    }
}
