package com.somemore.center.service.query;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.center.CenterJpaRepository;
import com.somemore.center.repository.center.CenterRepository;
import com.somemore.global.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class CenterSignServiceTest extends IntegrationTestSupport {

    @Autowired
    private CenterSignService centerSignService;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private CenterJpaRepository centerJpaRepository;

    @BeforeEach
    void cleanUp() {
        centerJpaRepository.deleteAll();
    }

    @DisplayName("계정 ID로 센터 ID를 조회할 수 있다.")
    @Test
    void getIdByAccountId() {
        //given
        Center center = createCenter();
        centerRepository.save(center);

        //when
        UUID centerId = centerSignService.getIdByAccountId("account123");

        //then
        assertThat(centerId)
                .isNotNull()
                .isEqualTo(center.getId());
    }

    @DisplayName("존재하지 않는 계정 ID로 센터 ID를 조회하면 예외가 발생한다.")
    @Test
    void getIdByNonExistentAccountId() {
        //given
        String nonExistentAccountId = "nonExistentAccount123";

        //when
        //then
        assertThatThrownBy(() -> centerSignService.getIdByAccountId(nonExistentAccountId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_EXISTS_CENTER.getMessage());
    }

    @DisplayName("계정 ID로 비밀번호를 조회할 수 있다.")
    @Test
    void getPasswordByAccountId() {
        //given
        Center center = createCenter();
        centerRepository.save(center);

        //when
        String password = centerSignService.getPasswordByAccountId("account123");

        //then
        assertThat(password)
                .isNotNull()
                .isEqualTo("password123");
    }

    @DisplayName("존재하지 않는 계정 ID로 비밀번호를 조회하면 예외가 발생한다.")
    @Test
    void getPasswordByNonExistentAccountId() {
        //given
        String nonExistentAccountId = "nonExistentAccount123";

        //when
        //then
        assertThatThrownBy(() -> centerSignService.getPasswordByAccountId(nonExistentAccountId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_EXISTS_CENTER.getMessage());
    }

    private static Center createCenter() {
        return Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com",
                "account123",
                "password123"
        );
    }
}