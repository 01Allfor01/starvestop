package com.allforone.starvestop.domain.apilog.repository;

import com.allforone.starvestop.domain.apilog.dto.ApiLogSearchCond;
import com.allforone.starvestop.domain.apilog.entity.ApiLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApiLogRepositoryCustom {

    Page<ApiLog> search(ApiLogSearchCond cond, Pageable pageable);
}
