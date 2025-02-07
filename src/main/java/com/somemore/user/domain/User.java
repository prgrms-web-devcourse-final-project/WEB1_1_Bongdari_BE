package com.somemore.user.domain;

import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "account_password", nullable = false)
    private String accountPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;


    public static User of(UserAuthInfo userAuthInfo, UserRole role) {
        return User.builder()
                .accountId(userAuthInfo.accountId())
                .accountPassword(userAuthInfo.accountPassword())
                .role(role)
                .build();
    }

    @Builder
    private User(String accountId, String accountPassword, UserRole role) {
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.role = role;
    }
}
