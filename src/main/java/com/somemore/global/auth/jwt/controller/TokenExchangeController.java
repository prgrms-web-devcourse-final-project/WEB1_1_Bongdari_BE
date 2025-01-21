package com.somemore.global.auth.jwt.controller;

import com.somemore.global.auth.annotation.CurrentUser;
import com.somemore.global.auth.annotation.UserId;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.manager.TokenManager;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/token")
@Tag(name = "Token Exchange API", description = "SignInToken을 AccessToken으로 교환하는 API")
public class TokenExchangeController {

    private final TokenManager tokenManager;

    @GetMapping("/exchange")
    public ApiResponse<String> exchangeToken(
            @UserId UUID userId
    ) {
        EncodedToken accessToken = tokenManager.getAccessTokenByUserId(userId);

        return ApiResponse.ok(HttpStatus.OK.value(),
                accessToken.getValueWithPrefix(),
                "액세스 토큰 응답 성공");
    }
}
