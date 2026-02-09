package com.allforone.starvestop.domain.store.repository;

import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import com.allforone.starvestop.domain.store.dto.response.StoreDto;
import org.springframework.data.domain.Slice;

import java.util.List;


public interface StoreRepositoryCustom {

    Slice<StoreDto> searchStoreSlice(SearchStoreCond searchStoreCond);

    List<Long> findActiveStoreIds();
}
