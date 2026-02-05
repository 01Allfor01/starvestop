package com.allforone.starvestop.domain.store.dto.response;

import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
    private final String ImageUrl;
}
