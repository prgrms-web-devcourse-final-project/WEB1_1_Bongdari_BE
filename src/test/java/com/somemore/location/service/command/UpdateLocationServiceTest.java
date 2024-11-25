package com.somemore.location.service.command;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.location.domain.Location;
import com.somemore.location.dto.request.LocationUpdateRequestDto;
import com.somemore.location.repository.LocationRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UpdateLocationServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateLocationService updateLocationService;

    @Autowired
    private LocationRepository locationRepository;

    private Location location;

    @BeforeEach
    void setUp() {
        location = Location.builder()
            .address("주소주소")
            .latitude(BigDecimal.valueOf(37.00000))
            .longitude(BigDecimal.valueOf(127.00000))
            .build();

        locationRepository.saveAndFlush(location);
    }

    @AfterEach
    void tearDown() {
        locationRepository.deleteAllInBatch();
    }

    @DisplayName("위치를 업데이트하면 저장소에 반영된다.")
    @Test
    void updateLocationWithDto() {
        // given
        BigDecimal latitude = BigDecimal.valueOf(37.111111);
        BigDecimal longitude = BigDecimal.valueOf(127.11111);
        LocationUpdateRequestDto dto = LocationUpdateRequestDto.builder()
            .address("새로새로")
            .latitude(latitude)
            .longitude(longitude)
            .build();

        // when
        updateLocationService.updateLocation(dto, location.getId());

        // then
        Location updateLocation = locationRepository.findById(location.getId()).orElseThrow();
        assertThat(updateLocation.getAddress()).isEqualTo(dto.address());
        assertThat(updateLocation.getLatitude().compareTo(dto.latitude())).isZero();
        assertThat(updateLocation.getLongitude().compareTo(dto.longitude())).isZero();
    }
}