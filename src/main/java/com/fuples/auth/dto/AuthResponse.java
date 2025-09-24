package com.fuples.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuples.auth.entity.Auth;
import com.fuples.user.enums.Provider;
import lombok.Builder;

import java.time.LocalDateTime;

public final class AuthResponse {

    private AuthResponse() {
    }

    @Builder
    public record Register(
            Long userId,
            String email,
            String name,
            Provider provider
    ) {
    }

    @Builder
    public record Token(
            String accessToken,
            String refreshToken,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime refreshTokenExpiresAt
    ) {
    }

    @Builder
    public record RefreshTokenInfo(
            Long id,
            Long userId,
            boolean revoked,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime expiresAt,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime createdAt,
            boolean expired
    ) {
        public static RefreshTokenInfo from(Auth token) {
            if (token == null) {
                return null;
            }
            return RefreshTokenInfo.builder()
                    .id(token.getId())
                    .userId(token.getUser() != null ? token.getUser().getUserId() : null)
                    .revoked(token.isRevoked())
                    .expiresAt(token.getExpiresAt())
                    .createdAt(token.getCreatedAt())
                    .expired(token.isExpired())
                    .build();
        }
    }
}