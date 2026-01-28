package com.allforone.starvestop.domain.subscription.repository;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByIsDeletedIsFalse();

    List<Subscription> findByStoreIdAndIsDeletedIsFalse(Long storeId);

    Optional<Subscription> findByIdAndIsDeletedIsFalse(Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Subscription s set s.stock = s.stock - 1 where s.id = :id and s.stock > 0")
    int decreaseStock(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Subscription s set s.stock = s.stock + 1 where s.id = :id")
    int increaseStock(@Param("id") Long id);

}
