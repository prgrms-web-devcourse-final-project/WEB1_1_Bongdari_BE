package com.somemore.center.controller;

import com.somemore.auth.signout.usecase.SignOutUseCase;
import com.somemore.center.dto.request.CenterSignRequestDto;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/center")
@Tag(name = "center Sign API", description = "기관 로그인, 로그아웃")
public class CenterSignController {

    private final SignOutUseCase signOutUseCase;

    @PostMapping("/sign-in")
    public ApiResponse<String> signIn(
            @RequestBody CenterSignRequestDto requestDto
    ) {

        return ApiResponse.ok("로그인되었습니다.");
    }

    @PostMapping("/sign-out")
    public ApiResponse<String> signOut(
            HttpServletResponse response,
            @AuthenticationPrincipal String userId) {

        signOutUseCase.signOut(response, userId);

        return ApiResponse.ok("로그아웃되었습니다");
    }
}
