package com.somemore.auth.oauth.naver.service.query;

import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.auth.oauth.naver.usecase.query.CheckNaverUserUseCase;
import com.somemore.auth.oauth.usecase.ProcessOAuthUserUseCase;
import com.somemore.auth.oauth.naver.dto.response.NaverUserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.somemore.auth.oauth.naver.util.OAuthResponseConverter.convertToNaverUserProfileResponseDto;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProcessNaverOAuthUserService implements ProcessOAuthUserUseCase {

    private final CheckNaverUserUseCase checkNaverUserUseCase;

    @Override
    public String processOAuthUser(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        return processUserInformation(oAuth2User);
    }

    private String processUserInformation(OAuth2User oAuth2User) {
        NaverUserProfileResponseDto dto = convertToNaverUserProfileResponseDto(oAuth2User);
        String oAuthId = dto.response().id();

        if (checkNaverUserUseCase.isNaverUserExists(oAuthId)) {
            return oAuthId;
        }

        log.error("유저가 회원 가입을 진행했으나, 존재하지 않는 상태입니다. OAuth Provider: {}, OAuth ID: {}",
                OAuthProvider.NAVER,
                oAuthId);
        throw new IllegalStateException();
    }
}
