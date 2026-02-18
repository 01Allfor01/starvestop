package com.allforone.starvestop.domain.notification.repository;

import com.allforone.starvestop.domain.notification.entity.UserNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.allforone.starvestop.domain.notification.entity.QUserNotification.userNotification;
import static com.allforone.starvestop.domain.order.entity.QOrder.order;
import static com.allforone.starvestop.domain.owner.entity.QOwner.owner;
import static com.allforone.starvestop.domain.store.entity.QStore.store;


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
