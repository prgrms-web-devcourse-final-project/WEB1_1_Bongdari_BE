package com.somemore.global.auth.oauth.handler;

import com.somemore.global.auth.cookie.CookieUseCase;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.UserRole;
import com.somemore.global.auth.jwt.usecase.GenerateTokensOnLoginUseCase;
import com.somemore.global.auth.oauth.OAuthProvider;
import com.somemore.global.auth.oauth.naver.service.query.ProcessNaverOAuthUserService;
import com.somemore.global.auth.redirect.RedirectUseCase;
import com.somemore.domains.volunteer.usecase.VolunteerQueryUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ProcessNaverOAuthUserService processNaverOAuthService;
    private final VolunteerQueryUseCase volunteerQueryUseCase;
    private final GenerateTokensOnLoginUseCase generateTokensOnLoginUseCase;
    private final CookieUseCase cookieUseCase;
    private final RedirectUseCase redirectUseCase;

    @Value("${app.front-url}")
    private String frontendRootUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String oAuthId;
        switch (getOAuthProvider(authentication)) {
            case NAVER -> oAuthId = processNaverOAuthService.processOAuthUser(authentication);
            default -> {
                log.error("지원하지 않는 OAuth 제공자입니다.");
                throw new IllegalArgumentException();
            }
        }

        UUID volunteerId = volunteerQueryUseCase.getVolunteerIdByOAuthId(oAuthId);
        EncodedToken accessToken =
                generateTokensOnLoginUseCase.saveRefreshTokenAndReturnAccessToken(
                        volunteerId, UserRole.VOLUNTEER
                );

        cookieUseCase.setAccessToken(response, accessToken.value());
        redirectUseCase.redirect(request, response, frontendRootUrl);
    }

    private static OAuthProvider getOAuthProvider(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken token) {
            return OAuthProvider.from(token.getAuthorizedClientRegistrationId());
        }
        throw new IllegalArgumentException();
    }
}
