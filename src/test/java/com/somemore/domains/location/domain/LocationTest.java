package com.somemore.domains.location.domain;

import com.somemore.domains.location.dto.request.LocationUpdateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;
import static org.assertj.core.api.Assertions.assertThat;

class LocationTest {

    @DisplayName("Location 생성시 소수점이 올바르게 반올림된다.")
    @Test
    void createLocation() {
        // given
        String address = "서울특별시 강남구";
        BigDecimal latitude = new BigDecimal("37.123456789");  // 소수점 초과값
        BigDecimal longitude = new BigDecimal("127.987654321"); // 소수점 초과값

        // when
        Location location = Location.builder()
            .address(address)
            .latitude(latitude)
            .longitude(longitude)
            .build();

        // then
        assertThat(location.getAddress()).isEqualTo(address);
        assertThat(location.getLatitude()).isEqualTo(latitude.setScale(8, HALF_UP));
        assertThat(location.getLongitude()).isEqualTo(longitude.setScale(8, HALF_UP));
    }

    @DisplayName("위치를 업데이트 할 수 있다.")
    @Test
    void updateLocationWithDto() {
        // given
        Location location = Location.builder()
            .address("서울특별시 서초구 반포대로 45, 4층(서초동, 명정빌딩)")
            .longitude(BigDecimal.valueOf(37.4845373748015))
            .latitude(BigDecimal.valueOf(127.010842267696))
            .build();

        LocationUpdateRequestDto requestDto = LocationUpdateRequestDto.builder()
            .address("업데이트 주소")
            .longitude(BigDecimal.valueOf(37.333333333333))
            .latitude(BigDecimal.valueOf(127.00000000000))
            .build();

        // when
        location.updateWith(requestDto);

        // then
        assertThat(location.getAddress()).isEqualTo(requestDto.address());
        assertThat(location.getLongitude()
            .compareTo(requestDto.longitude().setScale(8, HALF_UP))).isZero();
        assertThat(location.getLatitude()
            .compareTo(requestDto.latitude().setScale(8, HALF_UP))).isZero();
    }
}
