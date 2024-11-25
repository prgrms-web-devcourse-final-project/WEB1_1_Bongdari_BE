package com.somemore.auth.oauth.handler.success;

import com.somemore.auth.cookie.SetCookieUseCase;
import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.TokenType;
import com.somemore.auth.jwt.usecase.command.GenerateTokensOnLoginUseCase;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.auth.oauth.naver.service.query.ProcessNaverOAuthUserService;
import com.somemore.auth.redirect.RedirectUseCase;
import com.somemore.volunteer.usecase.query.FindVolunteerIdUseCase;
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
    private final FindVolunteerIdUseCase findVolunteerIdUseCase;
    private final GenerateTokensOnLoginUseCase generateTokensOnLoginUseCase;
    private final SetCookieUseCase setCookieUseCase;
    private final RedirectUseCase redirectUseCase;

    @Value("${frontend.url}")
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

        UUID volunteerId = findVolunteerIdUseCase.findVolunteerIdByOAuthId(oAuthId);
        EncodedToken accessToken = generateTokensOnLoginUseCase.saveRefreshTokenAndReturnAccessToken(volunteerId);

        setCookieUseCase.setToken(response, accessToken.value(), TokenType.ACCESS);
        redirectUseCase.redirect(request, response, frontendRootUrl);
    }

    private static OAuthProvider getOAuthProvider(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken token) {
            return OAuthProvider.from(token.getAuthorizedClientRegistrationId());
        }
        throw new IllegalArgumentException();
    }
}
