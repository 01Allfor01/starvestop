package com.allforone.starvestop.domain.store.dto;

import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreLimitedDto {
    private final Long id;
    private final String name;
    private final String address;
    private final StoreCategory category;
    private final Point location;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final StoreStatus status;
    private final String imageUuid;
    private final Double distance;
    private final LocalDateTime updatedAt;
}
