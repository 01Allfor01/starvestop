package com.allforone.starvestop.domain.store.repository;

import com.allforone.starvestop.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

//    Optional<Store> findByIdAndIsDeletedIsFalse(Long storeId);
//
//    List<Store> findAllByIsDeletedIsFalse();
}
