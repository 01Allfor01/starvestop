package com.allforone.starvestop.domain.order.repository;

import com.allforone.starvestop.domain.order.dto.OrderProductQuantityDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.allforone.starvestop.domain.order.entity.QOrderProduct.orderProduct;

@Repository
@RequiredArgsConstructor
public class OrderProductRepositoryImpl implements OrderProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderProductQuantityDto> findQuantityAndIdByOrderId(Long orderId) {
        return queryFactory
                .select(Projections.constructor(OrderProductQuantityDto.class,
                        orderProduct.id,
                        orderProduct.quantity
                ))
                .from(orderProduct)
                .where(orderProduct.order.id.eq(orderId))
                .fetch();
    }
}
