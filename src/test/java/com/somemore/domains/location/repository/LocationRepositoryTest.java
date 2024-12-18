package com.somemore.domains.location.repository;

import com.somemore.domains.location.domain.Location;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;
import static org.assertj.core.api.Assertions.assertThat;

class LocationRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private LocationRepository locationRepository;

    @AfterEach
    void tearDown() {
        locationRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("위도와 경도는 각각 소수점 8자리까지 반올림되어 저장된다.")
    void testLocationPrecisionWithRound() {
        // given
        Location locationWithRound = Location.builder()
            .address("서울특별시 서초구 반포대로 45, 4층(서초동, 명정빌딩)")
            .latitude(
                new BigDecimal("37.484537379").setScale(8, HALF_UP))  // 9자리 반올림
            .longitude(
                new BigDecimal("127.010842349").setScale(8, HALF_UP))  // 9자리 반올림
            .build();

        // when
        Location savedLocation = locationRepository.save(locationWithRound);

        // then
        assertThat(savedLocation.getLatitude()).isEqualTo(new BigDecimal("37.48453738"));
        assertThat(savedLocation.getLongitude()).isEqualTo(new BigDecimal("127.01084235"));
    }

    @Test
    @DisplayName("위도와 경도는 각각 소수점 8자리까지 정밀도를 보장하여 큰 값도 반올림한다.")
    void testLocationPrecisionWithLargeValues() {

        // given
        Location locationWithLargeValues = Location.builder()
            .address("서울특별시 서초구 반포대로 45, 4층(서초동, 명정빌딩)")
            .latitude(
                new BigDecimal("89.999999999").setScale(8, HALF_UP))  // 9자리
            .longitude(
                new BigDecimal("179.999999999").setScale(8, HALF_UP))  // 9자리
            .build();

        // when
        Location savedLocation = locationRepository.save(locationWithLargeValues);

        // then
        assertThat(savedLocation.getLatitude()).isEqualTo(new BigDecimal("90.00000000"));
        assertThat(savedLocation.getLongitude()).isEqualTo(new BigDecimal("180.00000000"));
    }
}
