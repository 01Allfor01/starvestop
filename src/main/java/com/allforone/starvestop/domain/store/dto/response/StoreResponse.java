package com.allforone.starvestop.domain.store.dto.response;

import com.allforone.starvestop.domain.store.dto.StoreRedisDto;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.geo.Point;

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
    private final Double distance;

    public static StoreResponse from(StoreRedisDto redisDto,
                                     Store store,
                                     String imageUrl) {

        Point point = redisDto.getPoint();

        return new StoreResponse(
                redisDto.getId(),
                store.getName(),
                store.getAddress(),
                store.getCategory(),
                point.getY(),
                point.getX(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getStatus(),
                imageUrl,
                redisDto.getDistance()
        );
    }
}
