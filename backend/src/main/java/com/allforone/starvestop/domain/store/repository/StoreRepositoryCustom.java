package com.allforone.starvestop.domain.store.repository;

import com.allforone.starvestop.domain.store.dto.StoreLimitedDto;
import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import org.springframework.data.domain.Slice;

import java.util.List;


public interface StoreRepositoryCustom {

    Slice<StoreLimitedDto> searchStoreSlice(SearchStoreCond searchStoreCond);

    List<Long> findActiveStoreIds();
}
