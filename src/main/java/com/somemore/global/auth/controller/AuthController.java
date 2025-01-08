package com.somemore.global.auth.controller;

import com.somemore.global.auth.annotation.CurrentUser;
import com.somemore.global.auth.dto.UserInfoResponseDto;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.usecase.AuthQueryUseCase;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthQueryUseCase authQueryUseCase;

    @GetMapping("/user-info")
    public ApiResponse<UserInfoResponseDto> getUserInfo(
            @CurrentUser UUID userId
    ) {
        UserRole role = authQueryUseCase.getRoleByUserId(userId);

        return ApiResponse.ok(HttpStatus.OK.value(),
                UserInfoResponseDto.from(userId, role),
                "유저 정보 응답 성공");
    }

    @GetMapping("/token")
    public ApiResponse<String> getToken(
            @CurrentUser UUID userId
    ) {
        EncodedToken accessToken = authQueryUseCase.getAccessTokenByUserId(userId);

        return ApiResponse.ok(HttpStatus.OK.value(),
                accessToken.getValueWithPrefix(),
                "액세스 토큰 응답 성공");
    }
}
