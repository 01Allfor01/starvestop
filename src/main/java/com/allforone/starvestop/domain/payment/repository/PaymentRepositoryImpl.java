package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.order.entity.QOrder;
import com.allforone.starvestop.domain.payment.dto.PaymentAggregate;
import com.allforone.starvestop.domain.payment.entity.QPayment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;

    @Override
    public int markClaimStockRelease(Long paymentId, List<PaymentStatus> allowedStatuses) {
        QPayment p = QPayment.payment;

        return (int) jpaQueryFactory
                .update(p)
                .set(p.stockReleased, true)
                .where(
                        p.id.eq(paymentId),
                        p.stockReleased.isFalse(),
                        p.status.in(allowedStatuses)
                )
                .execute();
    }

    @Override
    public PaymentAggregate aggregateGrossAmountByStoreAndPaidAt(
            Long storeId,
            PaymentStatus status,
            LocalDateTime start,
            LocalDateTime end
    ) {
        QPayment p = QPayment.payment;
        QOrder o = QOrder.order;

        return jpaQueryFactory
                .select(Projections.constructor(
                        PaymentAggregate.class,
                        Expressions.numberTemplate(BigDecimal.class, "sum({0})", p.amount)
                ))
                .from(p)
                .join(p.order, o)
                .where(
                        o.store.id.eq(storeId),
                        p.status.eq(status),
                        p.paymentAt.goe(start),
                        p.paymentAt.lt(end)
                )
                .fetchOne();
    }
}
