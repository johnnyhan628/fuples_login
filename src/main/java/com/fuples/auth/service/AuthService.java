package com.fuples.auth.service;

import com.fuples.auth.dto.AuthRequest;
import com.fuples.auth.dto.AuthResponse;
import com.fuples.auth.entity.Auth;
import com.fuples.auth.repo.AuthRepository;
import com.fuples.common.error.CustomException;
import com.fuples.common.error.ErrorCode;
import com.fuples.config.security.JwtUtil;
import com.fuples.user.entity.User;
import com.fuples.user.enums.Provider;
import com.fuples.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {

    private static final String BEARER_PREFIX = "Bearer ";

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Transactional
    public AuthResponse.Register register(AuthRequest.Register request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Provider provider = request.provider();
        String providerUserId = StringUtils.hasText(request.providerUserId())
                ? request.providerUserId()
                : request.email();

        User user = User.builder()
                .email(request.email())
                .passwordHash(encodedPassword)
                .name(request.name())
                .provider(provider)
                .providerUserId(providerUserId)
                .build();

        User saved = userRepository.save(user);

        return AuthResponse.Register.builder()
                .userId(saved.getUserId())
                .email(saved.getEmail())
                .name(saved.getName())
                .provider(saved.getProvider())
                .build();
    }

    @Transactional
    public AuthResponse.Token login(AuthRequest.Login request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_INVALID_CREDENTIALS));

        if (!StringUtils.hasText(user.getPasswordHash()) || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new CustomException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }

        revokeActiveTokens(user);

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        Auth savedRefreshToken = storeRefreshToken(user, refreshToken);

        return AuthResponse.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpiresAt(savedRefreshToken.getExpiresAt())
                .build();
    }

    @Transactional
    public AuthResponse.Token refresh(AuthRequest.Refresh request) {
        String normalizedToken = normalizeToken(request.refreshToken());
        if (!StringUtils.hasText(normalizedToken)) {
            throw new CustomException(ErrorCode.AUTH_REFRESH_REVOKED);
        }

        String tokenHash = hashToken(normalizedToken);
        Auth stored = authRepository.findByTokenHashAndRevokedFalse(tokenHash)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_REFRESH_REVOKED));

        if (stored.isExpired()) {
            stored.revoke();
            throw new CustomException(ErrorCode.AUTH_TOKEN_EXPIRED);
        }

        User user = stored.getUser();
        stored.revoke();

        String accessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        Auth saved = storeRefreshToken(user, newRefreshToken);

        return AuthResponse.Token.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .refreshTokenExpiresAt(saved.getExpiresAt())
                .build();
    }

    @Transactional
    public void logout(String token) {
        String normalized = normalizeToken(token);
        if (!StringUtils.hasText(normalized)) {
            return;
        }

        String tokenHash = hashToken(normalized);
        authRepository.findByTokenHash(tokenHash)
                .ifPresent(Auth::revoke);
    }

    private void revokeActiveTokens(User user) {
        List<Auth> activeTokens = authRepository.findAllByUserAndRevokedFalse(user);
        activeTokens.forEach(Auth::revoke);
    }

    private Auth storeRefreshToken(User user, String refreshToken) {
        String normalizedToken = normalizeToken(refreshToken);
        if (!StringUtils.hasText(normalizedToken)) {
            throw new CustomException(ErrorCode.AUTH_REFRESH_REVOKED);
        }

        Auth token = Auth.builder()
                .user(user)
                .tokenHash(hashToken(normalizedToken))
                .expiresAt(LocalDateTime.now().plus(Duration.ofMillis(jwtUtil.getRefreshExpirationTime())))
                .revoked(false)
                .build();

        return authRepository.save(token);
    }

    private String normalizeToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        String value = token.trim();
        if (value.startsWith(BEARER_PREFIX)) {
            value = value.substring(BEARER_PREFIX.length()).trim();
        }
        return value;
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to hash refresh token", e);
        }
    }
}