package com.somemore.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_setting")
public class UserSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "isAuthenticated", nullable = false)
    private boolean isAuthenticated;

    @Column(name = "isSmsAgreed", nullable = false)
    private boolean isSmsAgreed;


    public static UserSetting from(UUID userId) {
        return UserSetting.builder()
                .userId(userId)
                .isAuthenticated(false)
                .isSmsAgreed(false)
                .build();
    }

    @Builder
    private UserSetting(UUID userId, boolean isAuthenticated, boolean isSmsAgreed) {
        this.userId = userId;
        this.isAuthenticated = isAuthenticated;
        this.isSmsAgreed = isSmsAgreed;
    }
}
