package com.somemore.global.auth.sign.out;

import com.somemore.global.auth.jwt.refresh.manager.RefreshTokenManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SignOutService implements SignOutUseCase {

    private final RefreshTokenManager refreshTokenManager;

    @Override
    public void signOut(
            HttpServletResponse response,
            UUID userId) {

        refreshTokenManager.removeRefreshToken(userId.toString());
    }
}
