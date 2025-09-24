package com.fuples.user.entity;

import com.fuples.common.jpa.LocalDateTimeTextConverter;
import com.fuples.user.enums.Provider;
import com.fuples.user.enums.ProviderConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(
    name = "users",
    indexes = { @Index(name = "uk_users_email", columnList = "email", unique = true) }
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

    @Builder.Default
    @Column(name = "role", nullable = false, length = 20)
    private String role = "USER";

    @Convert(converter = ProviderConverter.class)
    @Column(name = "provider", nullable = false, length = 20)
    private Provider provider;

    @Column(name = "provider_id", length = 100)
    private String providerUserId;

    // TEXT 컬럼에 "yyyy-MM-dd HH:mm:ss.SSS" 로 저장/조회
    @Convert(converter = LocalDateTimeTextConverter.class)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Convert(converter = LocalDateTimeTextConverter.class)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(name = "token_version", nullable = false)
    private Integer tokenVersion = 0;

    /* === lifecycle === */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /* === helpers === */
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
