package com.fuples.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fuples.user.enums.Provider;
import com.fuples.user.enums.ProviderConverter;

import java.time.LocalDateTime;

@Table(
    name = "users",
    indexes = {
        @Index(name = "uk_users_email", columnList = "email", unique = true)
    }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * USER / ADMIN 만 저장하도록 사용처에서 보장
     * (Role enum이 있다면 @Enumerated(EnumType.STRING)으로 교체 권장)
     */

    @Builder.Default
    @Column(name = "role", nullable = false, length = 20)
    private String role = "USER";

    @Convert(converter = ProviderConverter.class)
    @Column(name = "provider", nullable = false, length = 20)
    private Provider provider;

    @Column(name = "provider_id", length = 100)
    private String providerUserId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(name = "token_version", nullable = false)
    private Integer tokenVersion = 0;

    public void increaseTokenVersion() {
        this.tokenVersion = (this.tokenVersion == null ? 1 : this.tokenVersion + 1);
    }

    public void changePasswordHash(String newHash) {
        this.passwordHash = newHash;
    }

    public void changeRole(String newRole) {
        this.role = newRole;
    }
}