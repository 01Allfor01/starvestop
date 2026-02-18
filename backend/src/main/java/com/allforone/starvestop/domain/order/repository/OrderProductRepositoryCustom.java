package com.allforone.starvestop.domain.order.repository;

import com.allforone.starvestop.domain.order.dto.OrderProductQuantityDto;

import java.util.List;

public interface OrderProductRepositoryCustom {
    List<OrderProductQuantityDto> findQuantityAndIdByOrderId(Long orderId);
}
