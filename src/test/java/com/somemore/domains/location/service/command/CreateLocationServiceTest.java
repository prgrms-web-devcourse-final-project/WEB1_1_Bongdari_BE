package com.somemore.domains.location.service.command;

import com.somemore.domains.location.domain.Location;
import com.somemore.domains.location.dto.request.LocationCreateRequestDto;
import com.somemore.domains.location.repository.LocationRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CreateLocationServiceTest extends IntegrationTestSupport {

    @Autowired
    private CreateLocationService createLocationService;

    @Autowired
    private LocationRepository locationRepository;

    @AfterEach
    void tearDown() {
        locationRepository.deleteAllInBatch();
    }

    @DisplayName("위치 생성 정보를 받아 위치를 저장한다")
    @Test
    void createLocationWithCreateRequestDto() {
        // given
        LocationCreateRequestDto dto = LocationCreateRequestDto.builder()
            .address("위치위치")
            .latitude(BigDecimal.valueOf(37.4845373748015))
            .longitude(BigDecimal.valueOf(127.010842267696))
            .build();

        // when
        Long locationId = createLocationService.createLocation(dto);

        // then
        Optional<Location> location = locationRepository.findById(locationId);
        assertThat(location).isPresent();
        assertThat(location.get().getId()).isEqualTo(locationId);
    }
}
