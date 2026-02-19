package com.allforone.starvestop.domain.notification.repository;

import com.allforone.starvestop.domain.notification.entity.UserNotification;
import com.allforone.starvestop.domain.subscription.enums.UserSubscriptionStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.allforone.starvestop.domain.notification.entity.QUserNotification.userNotification;
import static com.allforone.starvestop.domain.order.entity.QOrder.order;
import static com.allforone.starvestop.domain.owner.entity.QOwner.owner;
import static com.allforone.starvestop.domain.store.entity.QStore.store;
import static com.allforone.starvestop.domain.subscription.entity.QUserSubscription.userSubscription;


@Repository
@RequiredArgsConstructor
public class UserNotificationRepositoryImpl implements UserNotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public UserNotification findOwnerTokenByOrderId(Long orderId) {
        return queryFactory
                .select(userNotification)
                .from(order)
                .join(order.store, store)
                .join(store.owner, owner)
                .join(userNotification).on(userNotification.userId.eq(owner.id))
                .where(
                        order.id.eq(orderId),
                        owner.isDeleted.isFalse()
                ).fetchOne();

    }

    @Override
    public List<String> findUserTokenBySubscriptionId(Long subscriptionId) {
        return queryFactory
                .select(userNotification.token)
                .from(userSubscription)
                .join(userNotification).on(userNotification.userId.eq(userSubscription.user.id))
                .where(
                        userSubscription.subscription.id.eq(subscriptionId),
                        userSubscription.status.eq(UserSubscriptionStatus.ACTIVE)
                ).fetch();
    }
}
