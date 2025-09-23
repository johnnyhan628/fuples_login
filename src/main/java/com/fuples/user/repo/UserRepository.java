package com.fuples.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fuples.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByProviderUserId(String providerUserId);
}
