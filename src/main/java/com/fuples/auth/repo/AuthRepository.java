package com.fuples.auth.repo;

import com.fuples.auth.entity.Auth;
import com.fuples.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByTokenHash(String tokenHash);

    Optional<Auth> findByTokenHashAndRevokedFalse(String tokenHash);

    List<Auth> findAllByUserAndRevokedFalse(User user);
}