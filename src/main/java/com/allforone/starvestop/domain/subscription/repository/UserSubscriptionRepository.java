package com.allforone.starvestop.domain.subscription.repository;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.subscription.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    List<UserSubscription> findAllByUserAndIsDeletedIsFalse(User user);

    Optional<UserSubscription> findByIdAndIsDeletedIsFalse(Long userSubscriptionId);

    Optional<UserSubscription> findByUserAndSubscription(User user, Subscription subscription);

    boolean existsByUserAndSubscriptionAndIsDeletedIsFalse(User user, Subscription subscription);
}
