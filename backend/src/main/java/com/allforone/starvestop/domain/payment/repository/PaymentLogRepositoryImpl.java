package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.dto.response.SearchPaymentLogResponse;
import com.allforone.starvestop.domain.payment.entity.QPaymentLog;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentLogRepositoryImpl implements PaymentLogRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<SearchPaymentLogResponse> search(String orderKey, Long userId, Pageable pageable) {
        QPaymentLog pl = QPaymentLog.paymentLog;

        if (orderKey == null && userId == null) {
            return Page.empty(pageable);
        }

        List<SearchPaymentLogResponse> content = jpaQueryFactory
                .select(Projections.constructor(
                        SearchPaymentLogResponse.class,
                        pl.id,
                        pl.paymentId,
                        pl.userId,
                        pl.orderKey,
                        pl.timestamp
                ))
                .from(pl)
                .where(
                        orderKeyEq(orderKey, pl),
                        userIdEq(userId, pl)
                )
                .orderBy(pl.timestamp.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(pl.count())
                .from(pl)
                .where(
                        orderKeyEq(orderKey, pl),
                        userIdEq(userId, pl)
                )
                .fetchOne();

        long totalElements = (total == null) ? 0L : total;

        return new org.springframework.data.domain.PageImpl<>(content, pageable, totalElements);
    }


    private BooleanExpression orderKeyEq(String orderKey, QPaymentLog pl) {
        return (orderKey == null || orderKey.isBlank()) ? null : pl.orderKey.eq(orderKey);
    }

    private BooleanExpression userIdEq(Long userId, QPaymentLog pl) {
        return (userId == null) ? null : pl.userId.eq(userId);
    }

}
