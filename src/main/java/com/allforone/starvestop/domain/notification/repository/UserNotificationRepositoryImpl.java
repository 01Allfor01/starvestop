package com.allforone.starvestop.domain.notification.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserNotificationRepositoryImpl implements UserNotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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
