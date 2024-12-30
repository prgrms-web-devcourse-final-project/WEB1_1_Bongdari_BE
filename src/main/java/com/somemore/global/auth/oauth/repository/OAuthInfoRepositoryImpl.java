package com.somemore.global.auth.oauth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.global.auth.oauth.domain.OAuthInfo;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.global.auth.oauth.domain.QOAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OAuthInfoRepositoryImpl implements OAuthInfoRepository {

    private final JPAQueryFactory queryFactory;
    private final OAuthInfoJpaRepository oauthInfoJpaRepository;

    private static final QOAuthInfo oauthInfo = QOAuthInfo.oAuthInfo;

    @Override
    public Optional<UUID> findUserIdByOAuthProviderAndOauthId(OAuthProvider provider, String oauthId) {
        return Optional.ofNullable(
                queryFactory.select(oauthInfo.userId)
                        .from(oauthInfo)
                        .where(
                                oauthInfo.oauthId.eq(oauthId),
                                oauthInfo.oAuthProvider.eq(provider)
                        )
                        .fetchOne()
        );
    }

    @Override
    public boolean existByOAuthProviderAndOauthId(OAuthProvider provider, String oauthId) {
        return oauthInfoJpaRepository.existByOAuthProviderAndOauthId(provider, oauthId);
    }

    @Override
    public OAuthInfo save(OAuthInfo oauthInfo) {
        return oauthInfoJpaRepository.save(oauthInfo);
    }
}
