package com.fuples.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fuples.user.entity.User;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByProviderUserId(String providerUserId);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}