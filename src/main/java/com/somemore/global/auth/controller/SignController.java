package com.somemore.global.auth.controller;

import com.somemore.global.auth.dto.SignRequestDto;
import com.somemore.global.auth.signout.usecase.SignOutUseCase;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Sign API", description = "ID,PW 로그인, 로그아웃")
public class SignController {

    private final SignOutUseCase signOutUseCase;

    /*
     * ID 및 PW 기반 로그인 엔드포인트 (Swagger 문서화를 위한 엔드포인트)
     *
     * 이 엔드포인트는 실제로 작동하지 않으며, Spring Security 필터 체인에 의해 요청이 처리됩니다.
     * 로그인 요청은 필터에서 가로채어 인증 절차를 수행하며, 이 엔드포인트로 전달되지 않습니다.
     * 따라서 이 엔드포인트는 로그인 요청의 처리를 위한 진입점이 아니라,
     * Swagger API 문서화를 위해 정의된 엔드포인트입니다.
     *
     * 실제 로그인 절차는 필터에서 처리됩니다.
     */
    @PostMapping("/sign-in/id-pw")
    public ApiResponse<String> signIn(
            @RequestParam SignRequestDto signRequestDto
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
