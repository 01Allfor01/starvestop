package com.allforone.starvestop.domain.store.dto.response;

import com.allforone.starvestop.domain.store.dto.StoreDto;
import com.allforone.starvestop.domain.store.dto.StoreRedisDto;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
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
    private final String imageUrl;
    private final Double distance;
    private final LocalDateTime updatedAt;

    public static StoreResponse from(StoreDto storeDto,
                                     StoreRedisDto redisDto,
                                     String imageUrl) {
        return new StoreResponse(
                redisDto.getId(),
                storeDto.getName(),
                storeDto.getAddress(),
                storeDto.getCategory(),
                storeDto.getLocation().getY(),
                storeDto.getLocation().getX(),
                storeDto.getOpenTime(),
                storeDto.getCloseTime(),
                storeDto.getStatus(),
                imageUrl,
                redisDto.getDistance(),
                storeDto.getUpdatedAt()
        );
    }
}
