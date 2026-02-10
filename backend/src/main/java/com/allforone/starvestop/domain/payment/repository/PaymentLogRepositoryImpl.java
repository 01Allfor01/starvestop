package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.dto.response.SearchPaymentLogResponse;
import com.allforone.starvestop.domain.payment.entity.QPaymentLog;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentLogRepositoryImpl implements PaymentLogRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SearchPaymentLogResponse> search(String orderKey, Long userId) {
        QPaymentLog pl = QPaymentLog.paymentLog;

        if (orderKey == null && userId == null) {
            return List.of();
        }

        return jpaQueryFactory.select(
                        Projections.constructor(
                                SearchPaymentLogResponse.class,
                                pl.id,
                                pl.paymentId,
                                pl.userId,
                                pl.orderKey,
                                pl.pgStatus,
                                pl.timestamp
                        ))
                .from(pl)
                .where(orderKeyEq(orderKey, pl),
                        userIdEq(userId, pl))
                .orderBy(pl.timestamp.desc())
                .fetch();

    }

    private BooleanExpression orderKeyEq(String orderKey, QPaymentLog pl) {
        return (orderKey == null || orderKey.isBlank()) ? null : pl.orderKey.eq(orderKey);
    }

    private BooleanExpression userIdEq(Long userId, QPaymentLog pl) {
        return (userId == null) ? null : pl.userId.eq(userId);
    }
}
