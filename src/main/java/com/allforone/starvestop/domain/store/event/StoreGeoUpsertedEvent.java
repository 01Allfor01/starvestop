package com.allforone.starvestop.domain.store.event;

public record StoreGeoUpsertedEvent(Long storeId, double lon, double lat) {}
