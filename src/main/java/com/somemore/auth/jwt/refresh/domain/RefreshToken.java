package com.somemore.auth.jwt.refresh.domain;

import com.somemore.auth.jwt.domain.EncodedToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 7)
public class RefreshToken {

    @Id
    private String userId;

    @Indexed
    private String accessToken;
    private String refreshToken;

    @Builder
    public RefreshToken(String userId, EncodedToken accessToken, EncodedToken refreshToken) {
        this.userId = userId;
        this.accessToken = accessToken.value();
        this.refreshToken = refreshToken.value();
    }

    public void updateAccessToken(EncodedToken accessToken) {
        this.accessToken = accessToken.value();
    }
}
