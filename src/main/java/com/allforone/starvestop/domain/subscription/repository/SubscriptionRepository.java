package com.allforone.starvestop.domain.subscription.repository;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByIsDeletedIsFalse();

    List<Subscription> findByStoreIdAndIsDeletedIsFalse(Long storeId);

    Optional<Subscription> findByIdAndIsDeletedIsFalse(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Subscription> findByIdAndIsDeletedFalse(Long id);

    default Subscription getByIdWithoutLock(Long id) {
        return findByIdAndIsDeletedIsFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

    default Subscription getByIdWithLock(Long id) {
        return findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }
}
