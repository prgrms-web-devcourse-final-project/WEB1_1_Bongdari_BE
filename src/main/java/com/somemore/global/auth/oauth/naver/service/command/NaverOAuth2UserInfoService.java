package com.somemore.global.auth.oauth.naver.service.command;

import com.somemore.global.auth.oauth.naver.dto.response.NaverUserProfileResponseDto;
import com.somemore.global.auth.oauth.naver.usecase.query.CheckNaverUserUseCase;
import com.somemore.global.auth.oauth.naver.usecase.command.RegisterNaverUserUseCase;
import com.somemore.global.auth.oauth.naver.util.OAuthResponseConverter;
import com.somemore.volunteer.usecase.RegisterVolunteerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class NaverOAuth2UserInfoService {

    private final CheckNaverUserUseCase checkNaverUserUseCase;
    private final RegisterNaverUserUseCase registerNaverUserUseCase;
    private final RegisterVolunteerUseCase registerVolunteerUseCase;

    public OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        NaverUserProfileResponseDto dto = OAuthResponseConverter.convertToNaverUserProfileResponseDto(oAuth2User);
        String naverOauthId = dto.response().id();

        if (isNewUser(naverOauthId)) {
            registerUser(dto);
        }

        return oAuth2User;
    }

    private boolean isNewUser(String id) {
        return !checkNaverUserUseCase.isNaverUserExists(id);
    }

    private void registerUser(NaverUserProfileResponseDto dto) {
        registerNaverUserUseCase.registerNaverUser(dto.response().id());
        registerVolunteerUseCase.registerVolunteer(dto.toVolunteerRegisterRequestDto());
    }
}
