package com.allforone.starvestop.domain.store.repository;

import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import com.allforone.starvestop.domain.store.dto.response.StoreListResponse;

import java.util.List;

public interface StoreRepositoryCustom {

    List<StoreListResponse> searchStoreListResponse(SearchStoreCond searchStoreCond);
}
