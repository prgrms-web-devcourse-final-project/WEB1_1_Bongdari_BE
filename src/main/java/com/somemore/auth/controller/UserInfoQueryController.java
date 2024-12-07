package com.somemore.auth.controller;

import com.somemore.auth.dto.UserInfoResponseDto;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.somemore.auth.jwt.exception.JwtErrorType.INVALID_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class UserInfoQueryController {

    @GetMapping("/userinfo")
    public ApiResponse<UserInfoResponseDto> getUserInfoBySCH() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getPrincipal().toString();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new BadRequestException(INVALID_TOKEN.getMessage()));

        return ApiResponse.ok(200,
                new UserInfoResponseDto(userId, role),
                "유저 정보 응답 성공");
    }
}
