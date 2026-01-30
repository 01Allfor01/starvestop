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
public class StoreDto {
    private final Long id;
    private final String name;
    private final String address;
    private final StoreCategory category;
    private final Point location;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final StoreStatus status;
    private final String imageUuid;

    public static StoreDto from(Store store) {
        return new StoreDto(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getCategory(),
                store.getLocation(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getStatus(),
                store.getImageUuid()
        );
    }
}
