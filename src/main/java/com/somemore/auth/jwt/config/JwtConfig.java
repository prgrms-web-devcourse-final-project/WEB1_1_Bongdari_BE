package com.somemore.auth.jwt.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Bean
    public SecretKey secretKey(@Value("${jwt.secret}") String rawSecretKey) {
        return Keys.hmacShaKeyFor(rawSecretKey.getBytes(StandardCharsets.UTF_8));
    }
}