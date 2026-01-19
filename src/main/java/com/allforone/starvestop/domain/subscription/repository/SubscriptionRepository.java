package com.allforone.starvestop.domain.subscription.repository;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Slice<Subscription> findAllBy(Pageable pageable);

    Slice<Subscription> findAllByStoreId(Long storeId, Pageable pageable);
}
