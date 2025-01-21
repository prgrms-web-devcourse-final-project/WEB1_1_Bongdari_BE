package com.somemore.global.util.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptPasswordEncoderUtil {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private BCryptPasswordEncoderUtil() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static String encode(String rawPassword) {
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }
}
