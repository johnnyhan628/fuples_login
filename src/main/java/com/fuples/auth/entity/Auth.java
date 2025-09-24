package com.fuples.auth.entity;

import com.fuples.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Table(
    name = "refresh_tokens",
    indexes = {
        @Index(name = "idx_refresh_user", columnList = "user_id"),
        @Index(name = "idx_refresh_token_hash", columnList = "token_hash")
    }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_refresh_user")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "token_hash", nullable = false, length = 128)
    private String tokenHash;

    @Builder.Default
    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    // 만료 시각(반드시 서비스에서 세팅)
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    // ⬇︎ 여기서 NPE/제약 위반 났던 필드
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 저장 직전에 값이 비어 있으면 채운다 */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        // expiresAt 은 서비스에서 넣어오게 유지 (null이면 정책상 오류)
        if (expiresAt == null) {
            throw new IllegalStateException("expiresAt must be set before persisting Auth");
        }
    }

    public void revoke() {
        this.revoked = true;
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }
}
