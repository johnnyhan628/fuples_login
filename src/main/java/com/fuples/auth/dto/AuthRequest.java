package com.fuples.auth.dto;

import com.fuples.user.enums.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public final class AuthRequest {

    private AuthRequest() {
    }

    @Builder
    public record Register(
        @Email
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name,
        Provider provider,
        String providerUserId
    ) {
        public Register {
            provider = provider == null ? Provider.KAKAO : provider;
        }
    }

    @Builder
    public record Login(
        @Email
        @NotBlank
        String email,
        @NotBlank
        String password
    ) {
    }

    @Builder
    public record Refresh(
        @NotBlank
        String refreshToken
    ) {
    }

    @Builder
    public record Logout(
        String refreshToken
    ) {
    }
}