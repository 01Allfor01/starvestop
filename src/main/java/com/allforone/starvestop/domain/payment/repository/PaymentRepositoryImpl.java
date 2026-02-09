package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.entity.QPayment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
