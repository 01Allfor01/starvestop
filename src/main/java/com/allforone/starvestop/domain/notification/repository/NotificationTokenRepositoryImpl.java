package com.allforone.starvestop.domain.notification.repository;

import com.allforone.starvestop.domain.notification.entity.NotificationToken;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.allforone.starvestop.domain.notification.entity.QNotificationToken.notificationToken;
import static com.allforone.starvestop.domain.order.entity.QOrder.order;
import static com.allforone.starvestop.domain.store.entity.QStore.store;
import static com.allforone.starvestop.domain.owner.entity.QOwner.owner;


@Repository
@RequiredArgsConstructor
public class NotificationTokenRepositoryImpl implements NotificationTokenRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public NotificationToken findOwnerTokenByOrderId(Long orderId) {
        return queryFactory
                .select(notificationToken)
                .from(order)
                .join(order.store, store)
                .join(store.owner, owner)
                .join(notificationToken).on(notificationToken.userId.eq(owner.id))
                .where(
                        order.id.eq(orderId),
                        owner.isDeleted.isFalse()
                ).fetchOne();

    }

//    @Override
//    public List<SendMealTimeNotificationDto> findByMealTime(Integer day, Integer mealTime) {
//
//        BooleanExpression dayMatch = Expressions.numberTemplate(Integer.class, "({0} & {1})", subscription.day, day).ne(0);
//
//        BooleanExpression mealTimeMatch = Expressions.numberTemplate(Integer.class, "({0} & {1})", subscription.mealTime, mealTime).ne(0);
//
//        return queryFactory
//                .select(Projections.constructor(SendMealTimeNotificationDto.class,
//                        userNotification.token,
//                        subscription.name
//                ))
//                .from(userSubscription)
//                .join(userSubscription.subscription, subscription)
//                .join(userNotification).on(userNotification.user.id.eq(userSubscription.user.id))
//                .where(
//                        userSubscription.status.eq(UserSubscriptionStatus.ACTIVE),
//                        dayMatch,
//                        mealTimeMatch,
//                        userNotification.token.isNotNull()
//                )
//                .fetch();
//    }
}
