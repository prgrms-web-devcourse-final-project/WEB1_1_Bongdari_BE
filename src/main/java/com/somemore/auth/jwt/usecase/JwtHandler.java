package com.somemore.auth.jwt.usecase;

import com.somemore.auth.jwt.generator.JwtGenerator;
import com.somemore.auth.jwt.parser.JwtParser;
import com.somemore.auth.jwt.validator.JwtValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtHandler implements JwtUseCase{
    private final JwtGenerator jwtGenerator;
    private final JwtParser jwtParser;
    private final JwtValidator jwtValidator;

    public String generateToken(String userId, String role) {
        return jwtGenerator.generateToken(userId, role);
    }

    public boolean verifyToken(String token) {
        return jwtValidator.validateToken(token);
    }

    public String getClaimByKey(String token, String key) {
        return jwtParser.parseToken(token).get(key, String.class);
    }

}
