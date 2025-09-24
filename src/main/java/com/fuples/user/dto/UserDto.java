package com.fuples.user.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuples.user.entity.User;
import com.fuples.user.enums.Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long userId;

    private String email;

    private String name;

    private String role;             

    private Provider provider;

    private String providerUserId;

    private Integer tokenVersion;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public static UserDto from(User u) {
        if (u == null) return null;
        return UserDto.builder()
                .userId(u.getUserId())
                .email(u.getEmail())
                .name(u.getName())
                .role(u.getRole())
                .provider(u.getProvider())
                .providerUserId(u.getProviderUserId())
                .tokenVersion(u.getTokenVersion())
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }
}