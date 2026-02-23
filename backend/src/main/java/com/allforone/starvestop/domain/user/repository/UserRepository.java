package com.allforone.starvestop.domain.user.repository;

import com.allforone.starvestop.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByIdAndIsDeletedIsFalse(Long id);

    Optional<User> findByEmailAndIsDeletedIsFalse(String userEmail);

    User findByProviderIdAndIsDeletedIsFalse(Long providerId);

    @Query("SELECT u.userKey FROM User u WHERE u.id = :userId")
    String getUserKeyById(Long userId);
}
