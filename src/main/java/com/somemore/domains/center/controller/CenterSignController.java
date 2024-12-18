package com.somemore.domains.center.controller;

import com.somemore.domains.center.dto.request.CenterSignRequestDto;
import com.somemore.global.auth.signout.usecase.SignOutUseCase;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
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

    /*
     * 기관 로그인 엔드포인트
     * 실제 로그인 절차는 필터에서 처리됩니다.
     * 이 엔드포인트는 로그인 요청을 받아 필터에 의한 인증 절차를 수행합니다.
     */
    @PostMapping("/sign-in")
    public ApiResponse<String> signIn(
            @RequestParam CenterSignRequestDto requestDto
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
