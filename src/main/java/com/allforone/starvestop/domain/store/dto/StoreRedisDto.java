package com.allforone.starvestop.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.geo.Point;

@Getter
@AllArgsConstructor
public class StoreRedisDto {
    private final Long id;
    private final Point point;
    private final Double distance;
}
