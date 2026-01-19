package com.allforone.starvestop.domain.subscription.repository;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByIsDeletedIsFalse();

    List<Subscription> findByStoreIdAndIsDeletedIsFalse(Long storeId);

    Optional<Subscription> findByIdAndIsDeletedIsFalse(Long id);
}
