package com.allforone.starvestop.domain.owner.repository;

import com.allforone.starvestop.domain.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
