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

    @Transactional
    @Override
    public int markFailedAndClaimStockRelease(//결제 상태 실패로 만들고, stockRelease true변경
            Long paymentId,
            PaymentStatus failedStatus,
            List<PaymentStatus> releasableStatuses
    ) {
        QPayment p = QPayment.payment;

        em.flush();

        long updated = jpaQueryFactory
                .update(p)
                .set(p.status, failedStatus)
                .set(p.stockReleased, true)
                .where(
                        p.id.eq(paymentId),
                        p.stockReleased.isFalse(),
                        p.status.in(releasableStatuses)
                ).execute();

        em.clear();

        return (int) updated;
    }
}
