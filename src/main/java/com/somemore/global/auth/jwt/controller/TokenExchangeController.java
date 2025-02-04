package com.somemore.global.auth.jwt.controller;

import com.somemore.global.auth.annotation.UserId;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.dto.AccessTokenRequestDto;
import com.somemore.global.auth.jwt.manager.TokenManager;
import com.somemore.global.auth.jwt.usecase.JwtUseCase;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/token")
@Tag(name = "Token API", description = "JWT 관련 API")
public class TokenExchangeController {

    private final TokenManager tokenManager;
    private final JwtUseCase jwtUseCase;

    @GetMapping("/exchange")
    public ApiResponse<String> exchangeToken(
            @UserId UUID userId
    ) {
        EncodedToken accessToken = tokenManager.getAccessTokenByUserId(userId);

        return ApiResponse.ok(HttpStatus.OK.value(),
                accessToken.getValueWithPrefix(),
                "액세스 토큰 교환 성공");
    }

    @PostMapping("/refresh")
    public ApiResponse<String> refreshAccessToken(
            @Valid @RequestBody AccessTokenRequestDto accessTokenRequestDto
    ) {
        EncodedToken newAccessToken = jwtUseCase.refreshAccessToken(
                EncodedToken.from(accessTokenRequestDto.accessToken()));

        return ApiResponse.ok(HttpStatus.OK.value(),
                newAccessToken.getValueWithPrefix(),
                "액세스 토큰 갱신 성공");
    }


}
