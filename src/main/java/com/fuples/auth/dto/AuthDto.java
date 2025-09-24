package com.fuples.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuples.auth.entity.Auth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {

    private Long id;

    private Long userId;

    private String tokenHash;

    private boolean revoked;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiresAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    private boolean expired;

    public static AuthDto from(Auth a) {
        if (a == null) return null;
        boolean isExpired = a.getExpiresAt() != null && a.getExpiresAt().isBefore(LocalDateTime.now());
        return AuthDto.builder()
                .id(a.getId())
                .userId(a.getUser() != null ? a.getUser().getUserId() : null)
                // .tokenHash(a.getTokenHash()) // 보안상 비권장
                .revoked(a.isRevoked())
                .expiresAt(a.getExpiresAt())
                .createdAt(a.getCreatedAt())
                .expired(isExpired)
                .build();
    }
}