package com.somemore.auth.jwt.refresh.repository;

import com.somemore.auth.jwt.refresh.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByAccessToken(String accessToken);
}
