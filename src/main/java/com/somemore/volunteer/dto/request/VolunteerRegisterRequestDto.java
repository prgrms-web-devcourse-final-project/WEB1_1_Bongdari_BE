package com.somemore.volunteer.dto.request;

import com.somemore.global.auth.oauth.OAuthProvider;

public record VolunteerRegisterRequestDto(
        OAuthProvider oAuthProvider,
        String oauthId,
        String name,
        String email,
        String gender,
        String birthday,
        String birthyear,
        String mobile
) {
}
