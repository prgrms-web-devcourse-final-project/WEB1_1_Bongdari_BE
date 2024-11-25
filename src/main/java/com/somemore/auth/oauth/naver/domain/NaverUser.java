package com.somemore.auth.oauth.naver.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "naver_user")
public class NaverUser {
    @Id
    private String oauthId;

    private NaverUser(String oauthId) {
        this.oauthId = oauthId;
    }

    public static NaverUser from(String oauthId) {
        return new NaverUser(oauthId);
    }
}
