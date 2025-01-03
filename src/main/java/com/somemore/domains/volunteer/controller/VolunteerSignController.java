package com.somemore.domains.volunteer.controller;

import com.somemore.domains.volunteer.usecase.GenerateOAuthUrlUseCase;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.auth.signout.usecase.SignOutUseCase;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/volunteer")
@Tag(name = "Volunteer Sign API", description = "봉사자 로그인, 로그아웃")
public class VolunteerSignController {

    private final GenerateOAuthUrlUseCase generateOAuthUrlUseCase;
    private final SignOutUseCase signOutUseCase;

    @PostMapping("/sign-in/oauth/{oauthProvider}")
    public RedirectView signIn(
            @Parameter(name = "oauthProvider", description = "OAuth 제공자 선택", example = "naver", required = true, schema = @Schema(allowableValues = {"naver"}))
            @PathVariable("oauthProvider") String oauthProvider) {

        String redirectUrl = switch (oauthProvider.toLowerCase()) {
            case "naver" -> generateOAuthUrlUseCase.generateUrl(oauthProvider);

            default -> throw new BadRequestException("지원되지 않는 OAuth 제공자: " + oauthProvider);
        };

        return new RedirectView(redirectUrl);
    }

    @PostMapping("/sign-out")
    public ApiResponse<String> signOut(
            HttpServletResponse response,
            @AuthenticationPrincipal String userId) {

        signOutUseCase.signOut(response, userId);

        return ApiResponse.ok("로그아웃되었습니다");
    }
}
