package com.somemore.global.auth.oauth.handler;

import com.somemore.global.auth.cookie.CookieUseCase;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.usecase.GenerateTokensOnLoginUseCase;
import com.somemore.global.auth.oauth.processor.OAuthUserProcessor;
import com.somemore.global.auth.redirect.RedirectUseCase;
import com.somemore.user.domain.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuthUserProcessor oauthUserProcessor;
    private final GenerateTokensOnLoginUseCase generateTokensOnLoginUseCase;
    private final CookieUseCase cookieUseCase;
    private final RedirectUseCase redirectUseCase;

    public static final String SUCCESS_PATH = "/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        OAuth2User oauthUser = extractOAuthUser(authentication);
        UUID userId = oauthUserProcessor.fetchUserIdByOAuthUser(oauthUser);

        processAccessToken(response, userId);
        redirect(request, response);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response) {
        // TODO 유저 정보 커스텀 확인 분기
        redirectUseCase.redirect(request, response, SUCCESS_PATH);
    }

    private void processAccessToken(HttpServletResponse response, UUID userId) {
        generateTokensOnLoginUseCase.generateAuthTokensAndReturnAccessToken(
                userId, UserRole.getOAuthUserDefaultRole());

        EncodedToken loginToken =
                generateTokensOnLoginUseCase.generateLoginToken(
                        userId, UserRole.getOAuthUserDefaultRole());

        cookieUseCase.setToken(response, loginToken.getValueWithPrefix(), TokenType.SIGN_IN);
    }

    private OAuth2User extractOAuthUser(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken token) {
            return token.getPrincipal();
        }
        log.error("Authentication 객체가 OAuth2AuthenticationToken 타입이 아닙니다: {}", authentication.getClass().getName());
        throw new IllegalArgumentException("잘못된 인증 객체입니다.");
    }
}
