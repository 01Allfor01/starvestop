package com.allforone.starvestop.domain.user.repository;

import com.allforone.starvestop.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
