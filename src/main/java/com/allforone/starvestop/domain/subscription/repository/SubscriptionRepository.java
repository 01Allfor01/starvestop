package com.allforone.starvestop.domain.subscription.repository;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByIsDeletedIsFalse();

    List<Subscription> findByStoreIdAndIsDeletedIsFalse(Long storeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Subscription> findByIdAndIsDeletedIsFalse(Long id);
}
