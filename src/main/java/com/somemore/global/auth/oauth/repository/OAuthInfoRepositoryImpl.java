package com.somemore.global.auth.oauth.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
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
                                eqOAuthInfo(provider, oauthId),
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    @Override
    public boolean existsByOAuthProviderAndOauthId(OAuthProvider provider, String oauthId) {
        return queryFactory.selectOne()
                .from(oauthInfo)
                .where(
                        eqOAuthInfo(provider, oauthId),
                        isNotDeleted()
                )
                .fetchFirst() != null;
    }

    @Override
    public OAuthInfo save(OAuthInfo oauthInfo) {
        return oauthInfoJpaRepository.save(oauthInfo);
    }

    private BooleanExpression eqOAuthInfo(OAuthProvider provider, String oauthId) {
        return oauthInfo.oauthId.eq(oauthId)
                .and(oauthInfo.oAuthProvider.eq(provider));
    }

    private BooleanExpression isNotDeleted() {
        return oauthInfo.deleted.eq(false);
    }

}
