package com.allforone.starvestop.domain.subscription.dto;

import java.math.BigDecimal;

public record SubscriptionDto(Long id,
                              Long storeId,
                              String storeName,
                              String name,
                              String description,
                              Integer day,
                              Integer mealTime,
                              BigDecimal price,
                              Integer stock,
                              Boolean isJoinable) {
}
