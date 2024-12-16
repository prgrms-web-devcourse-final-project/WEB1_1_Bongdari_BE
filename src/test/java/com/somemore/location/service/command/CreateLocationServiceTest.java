package com.somemore.location.service.command;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.location.domain.Location;
import com.somemore.location.dto.request.LocationCreateRequestDto;
import com.somemore.location.repository.LocationRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
