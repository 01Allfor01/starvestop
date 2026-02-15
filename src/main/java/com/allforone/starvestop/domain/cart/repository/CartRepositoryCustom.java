package com.allforone.starvestop.domain.cart.repository;

import com.allforone.starvestop.domain.cart.dto.CartListResponse;

public interface CartRepositoryCustom {
    CartListResponse findCartListAndStoreIdByUserId(Long userId);
}
