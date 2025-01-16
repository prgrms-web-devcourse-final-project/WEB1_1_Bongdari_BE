package com.somemore.global.auth.jwt.repository;

import com.somemore.global.auth.jwt.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByAccessToken(String accessToken);

    Optional<RefreshToken> findByUserId(String userId);
}
