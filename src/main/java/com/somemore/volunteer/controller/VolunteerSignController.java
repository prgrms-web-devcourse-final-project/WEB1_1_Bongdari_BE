package com.somemore.volunteer.controller;

import com.somemore.global.common.response.ApiResponse;
import com.somemore.global.exception.BadRequestException;
import com.somemore.volunteer.usecase.SignOutVolunteerUseCase;
import com.somemore.volunteer.usecase.GenerateOAuthUrlUseCase;
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
@Tag(name = "Volunteer OAuth API", description = "Handles Volunteer OAuth Sign-in, Sign-out")
public class VolunteerSignController {

    private final GenerateOAuthUrlUseCase generateOAuthUrlUseCase;
    private final SignOutVolunteerUseCase signOutVolunteerUseCase;

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
    public ApiResponse<?> signOut(
            HttpServletResponse response,
            @AuthenticationPrincipal String userId) {

        signOutVolunteerUseCase.signOut(response, userId);

        return ApiResponse.ok("로그아웃되었습니다");
    }
}
