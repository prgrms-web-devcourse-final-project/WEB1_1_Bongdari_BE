package com.somemore.global.util.encoder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class BCryptPasswordEncoderUtilTest {

    @Test
    @DisplayName("비밀번호를 성공적으로 인코딩할 수 있다")
    void testEncode() {
        // given
        String rawPassword = "password123";

        // when
        String encodedPassword = BCryptPasswordEncoderUtil.encode(rawPassword);

        // then
        assertThat(encodedPassword).isNotNull();

    }

    @Test
    @DisplayName("비밀번호를 성공적으로 매칭시킬 수 있다")
    void testMatches() {
        // given
        String rawPassword = "password123";

        // when
        String encodedPassword = BCryptPasswordEncoderUtil.encode(rawPassword);

        // then
        assertThat(BCryptPasswordEncoderUtil.matches(rawPassword, encodedPassword)).isTrue();
    }
}