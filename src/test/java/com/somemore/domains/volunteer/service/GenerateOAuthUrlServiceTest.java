package com.somemore.domains.volunteer.service;

import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

class GenerateOAuthUrlServiceTest extends IntegrationTestSupport {

    @Autowired
    private GenerateOAuthUrlService generateOAuthUrlService;

    @Value("${app.back-url}")
    private String backendRootUrl;


    @Test
    void generateUrl_ShouldReturnCorrectUrl_ForNaver() {
        // Given
        String oAuthProvider = OAuthProvider.NAVER.getProviderName();

        // When
        String result = generateOAuthUrlService.generateUrl(oAuthProvider);

        // Then
        String expectedUrl = backendRootUrl + "/oauth2/authorization/naver";

        assertThat(result).isEqualTo(expectedUrl);
    }

    @Test
    void generateUrl_ShouldReturnCorrectUrl_ForKakao() {
        // Given
        String oAuthProvider = OAuthProvider.KAKAO.getProviderName();

        // When
        String result = generateOAuthUrlService.generateUrl(oAuthProvider);

        // Then
        String expectedUrl = backendRootUrl + "/oauth2/authorization/kakao";

        assertThat(result).isEqualTo(expectedUrl);
    }
}
