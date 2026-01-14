package com.allforone.starvestop.domain.store.dto;

import com.allforone.starvestop.domain.store.enums.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class CreateStoreRequest {
    private String storeName;
    private String address;
    private String description;
    private StoreCategory category;
    private LocalTime openTime;
    private LocalTime closeTime;
}
