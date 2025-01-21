package com.somemore.global.auth.oauth.handler;

import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.cookie.CookieUseCase;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.usecase.GenerateTokensOnLoginUseCase;
import com.somemore.global.auth.oauth.domain.CustomOAuth2User;
import com.somemore.global.auth.oauth.processor.OAuthUserProcessor;
import com.somemore.global.auth.redirect.RedirectUseCase;
import com.somemore.user.domain.UserRole;
import com.somemore.volunteer.usecase.NEWVolunteerQueryUseCase;
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
    private final NEWVolunteerQueryUseCase volunteerQueryUseCase;
    private final GenerateTokensOnLoginUseCase generateTokensOnLoginUseCase;
    private final CookieUseCase cookieUseCase;
    private final RedirectUseCase redirectUseCase;

    public static final String SUCCESS_PATH = "/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        CustomOAuth2User oauthUser = extractOAuthUser(authentication);

        UUID userId = oauthUserProcessor.fetchUserIdByOAuthUser(oauthUser);
        UUID volunteerId = volunteerQueryUseCase.getIdByUserId(userId);
        UserRole role = UserRole.getOAuthUserDefaultRole();

        UserIdentity userIdentity = UserIdentity.of(userId, volunteerId, role);

        processToken(response, userIdentity);
        redirect(request, response);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response) {
        // TODO 유저 정보 커스텀 확인 분기
        redirectUseCase.redirect(request, response, SUCCESS_PATH);
    }

    private void processToken(HttpServletResponse response, UserIdentity userIdentity) {
        generateTokensOnLoginUseCase.generateAuthTokensAndReturnAccessToken(userIdentity);

        EncodedToken loginToken = generateTokensOnLoginUseCase.generateLoginToken(userIdentity);

        cookieUseCase.setToken(response, loginToken.value(), TokenType.SIGN_IN);
    }

    private CustomOAuth2User extractOAuthUser(Authentication authentication) {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = castToOAuth2AuthenticationTokenBy(authentication);
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        return castToCustomOAuth2UserBy(oAuth2User);
    }

    private OAuth2AuthenticationToken castToOAuth2AuthenticationTokenBy(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken token) {
            return token;
        }
        log.error("Authentication 객체가 OAuth2AuthenticationToken 타입이 아닙니다: {}", authentication.getClass().getName());
        throw new IllegalArgumentException();
    }

    private CustomOAuth2User castToCustomOAuth2UserBy(OAuth2User oAuth2User) {
        if (oAuth2User instanceof CustomOAuth2User customOAuth2User) {
            return customOAuth2User;
        }
        log.error("OAuth2User 객체가 CustomOAuth2User 타입이 아닙니다: {}", oAuth2User.getClass().getName());
        throw new IllegalArgumentException();
    }
}
