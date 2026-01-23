package com.allforone.starvestop.domain.owner.repository;

import com.allforone.starvestop.domain.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    boolean existsByEmail(String ownerEmail);

    Optional<Owner> findByEmailAndIsDeletedIsFalse(String userEmail);

    Optional<Owner> findByIdAndIsDeletedIsFalse(Long ownerId);
}
