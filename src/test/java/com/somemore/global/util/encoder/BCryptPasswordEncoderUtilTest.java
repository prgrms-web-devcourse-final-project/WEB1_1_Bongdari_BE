package com.somemore.global.util.encoder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("인스턴스화 할 수 없다.")
    void testConstruct() throws Exception {
        // given
        var constructor = BCryptPasswordEncoderUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when & then
        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .extracting(Throwable::getCause)
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
