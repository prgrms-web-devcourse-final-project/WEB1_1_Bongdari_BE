package com.somemore.global.develop.controller;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.domain.UserRole;
import com.somemore.global.auth.jwt.generator.JwtGenerator;
import com.somemore.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/develop/token")
@RequiredArgsConstructor
public class TokenGenerateMachine {

    private final JwtGenerator jwtGenerator;

    @GetMapping("/volunteer/{volunteerId}")
    public ApiResponse<String> getVolunteerToken(@PathVariable String volunteerId) {
        EncodedToken token = jwtGenerator.generateToken(volunteerId, UserRole.VOLUNTEER.getAuthority(), TokenType.REFRESH);
        return ApiResponse.ok(token.value());
    }

    @GetMapping("/center/{centerId}")
    public ApiResponse<String> getCenterToken(@PathVariable String centerId) {
        EncodedToken token = jwtGenerator.generateToken(centerId, UserRole.CENTER.getAuthority(), TokenType.REFRESH);
        return ApiResponse.ok(token.value());
    }
}
