package com.allforone.starvestop.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreRedisDto {
    private final Long id;
    private final Double distance;
}
