package com.allforone.starvestop.domain.store.repository;

import com.allforone.starvestop.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
