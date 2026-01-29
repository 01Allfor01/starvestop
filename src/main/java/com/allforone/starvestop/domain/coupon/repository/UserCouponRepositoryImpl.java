package com.allforone.starvestop.domain.coupon.repository;

import com.allforone.starvestop.domain.coupon.entity.UserCoupon;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.allforone.starvestop.domain.coupon.entity.QUserCoupon.userCoupon;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements  UserCouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserCoupon> findActiveCouponList(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return queryFactory
                .selectFrom(userCoupon)
                .where(
                        userCoupon.user.id.eq(userId),
                        userCoupon.isDeleted.isFalse(),
                        userCoupon.usedAt.isNull(),
                        userCoupon.startedAt.before(now),
                        userCoupon.expiresAt.after(now)
                )
                .fetch();
    }
}
