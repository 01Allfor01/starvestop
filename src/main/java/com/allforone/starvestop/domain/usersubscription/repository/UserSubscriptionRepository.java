package com.allforone.starvestop.domain.usersubscription.repository;

import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
}
