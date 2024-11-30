package com.somemore.auth.util;

import static com.somemore.auth.oauth.OAuthProvider.NAVER;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.TokenType;
import com.somemore.auth.jwt.domain.UserRole;
import com.somemore.auth.jwt.generator.JwtGenerator;
import com.somemore.auth.jwt.refresh.domain.RefreshToken;
import com.somemore.auth.jwt.refresh.manager.RefreshTokenManager;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.CenterJpaRepository;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerJpaRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DevAccountSetUpConfig {

    private final VolunteerJpaRepository volunteerRepository;
    private final CenterJpaRepository centerRepository;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenManager refreshTokenManager;

    private Volunteer volunteer;
    private Center center;

    @Value("${app.develop.mode:false}")
    private boolean developMode;

    @PostConstruct
    public void generateAccessTokenForDev() {
        if (!developMode) {
            return; // 개발 모드에서만 실행
        }

        volunteer = Volunteer.createDefault(NAVER, "bongdari");
        center = Center.create(
                "봉다리 자원봉사센터",
                "02-1234-5678",
                "",
                "봉다리 기관 테스트 계정입니다.",
                "https://somemore.bongdari.com",
                "bongdari",
                "1234"
        );

        volunteer = volunteerRepository.findByOauthId(volunteer.getOauthId())
                .orElseGet(() -> volunteerRepository.save(volunteer));

        center = centerRepository.findByName(center.getName())
                .orElseGet(() -> centerRepository.save(center));

        EncodedToken volunteerToken = saveRefreshTokenAndReturnAccessToken(volunteer.getId(),
                UserRole.VOLUNTEER);
        EncodedToken centerToken = saveRefreshTokenAndReturnAccessToken(center.getId(),
                UserRole.CENTER);

        log.info("테스트용 봉사자 AccessToken: {}", volunteerToken.value());
        log.info("테스트용 기관 AccessToken: {}", centerToken.value());
    }

    @PreDestroy
    public void cleanup() {
        if (volunteer != null) {
            refreshTokenManager.removeRefreshToken(volunteer.getId().toString());
            log.info("테스트용 AccessToken 제거, 봉사자 ID: {}", volunteer.getId());
        }
        if (center != null) {
            refreshTokenManager.removeRefreshToken(center.getId().toString());
            log.info("테스트용 AccessToken 제거, 기관 ID: {}", center.getId());
        }
    }

    private EncodedToken saveRefreshTokenAndReturnAccessToken(UUID id, UserRole role) {
        EncodedToken accessToken = generateToken(id, role, TokenType.ACCESS);
        RefreshToken refreshToken = generateRefreshToken(id, role, accessToken);
        refreshTokenManager.save(refreshToken);
        return accessToken;
    }

    private EncodedToken generateToken(UUID id, UserRole role, TokenType tokenType) {
        return jwtGenerator.generateToken(id.toString(), role.name(), tokenType);
    }

    private RefreshToken generateRefreshToken(UUID id, UserRole role, EncodedToken accessToken) {
        return new RefreshToken(id.toString(), accessToken,
                generateToken(id, role, TokenType.REFRESH));
    }
}

