package com.somemore.volunteer.controller;

import com.somemore.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VolunteerSignControllerTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("유효한 OAuth 제공자로 로그인 URL을 생성한다.")
    void signInWithValidProvider() throws Exception {
        // Given
        String oauthProvider = "naver";

        // When
        // Then
        mockMvc.perform(post("/api/volunteer/sign-in/oauth/{oauthProvider}", oauthProvider))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    String redirectedUrl = response.getRedirectedUrl();
                    assertThat(redirectedUrl).isNotNull();
                    assertThat(redirectedUrl).contains("oauth2/authorization/naver");
                });
    }

    @Test
    @DisplayName("지원되지 않는 OAuth 제공자로 로그인 시 400 에러를 반환한다.")
    void signInWithInvalidProvider() throws Exception {
        // Given
        String invalidProvider = "unsupported-provider";

        // When
        // Then
        mockMvc.perform(post("/api/volunteer/sign-in/oauth/{oauthProvider}", invalidProvider))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그아웃 요청 시 성공 메시지를 반환한다.")
    void signOut() throws Exception {
        // When
        // Then
        mockMvc.perform(post("/api/volunteer/sign-out"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    String responseBody = response.getContentAsString();
                    assertThat(responseBody).contains("로그아웃되었습니다");
                });
    }
}