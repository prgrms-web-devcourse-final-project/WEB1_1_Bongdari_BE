package com.somemore.auth.signout.service;

import com.somemore.auth.cookie.CookieUseCase;
import com.somemore.auth.jwt.refresh.manager.RefreshTokenManager;
import com.somemore.auth.signout.usecase.SignOutUseCase;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SignOutService implements SignOutUseCase {

    private final CookieUseCase cookieUseCase;
    private final RefreshTokenManager refreshTokenManager;

    @Override
    public void signOut(
            HttpServletResponse response,
            String volunteerId) {

        cookieUseCase.deleteAccessToken(response);
        refreshTokenManager.removeRefreshToken(volunteerId);
    }
}
