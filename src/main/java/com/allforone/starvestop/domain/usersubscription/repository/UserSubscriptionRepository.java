package com.allforone.starvestop.domain.usersubscription.repository;

import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    List<UserSubscription> findAllByUserId(Long userId);
}
