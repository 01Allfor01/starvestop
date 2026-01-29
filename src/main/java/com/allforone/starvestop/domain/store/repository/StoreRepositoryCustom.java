package com.allforone.starvestop.domain.store.repository;

import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import com.allforone.starvestop.domain.store.dto.response.StoreDto;
import org.springframework.data.domain.Page;


public interface StoreRepositoryCustom {

    Page<StoreDto> searchStorePage(SearchStoreCond searchStoreCond);
}
