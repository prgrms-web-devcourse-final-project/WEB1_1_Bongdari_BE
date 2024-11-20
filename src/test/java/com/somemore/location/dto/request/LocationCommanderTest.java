package com.somemore.location.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.location.domain.Location;
import com.somemore.location.dto.request.LocationCreateRequestDto;
import com.somemore.location.repository.LocationRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LocationCommanderTest extends IntegrationTestSupport {

    @Autowired
    private LocationCommander locationCommander;

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
            .latitude("위도위도")
            .longitude("경도경도")
            .build();

        // when
        Long locationId = locationCommander.createLocation(dto);
        Optional<Location> location = locationRepository.findById(locationId);

        // then
        assertThat(location.isPresent()).isTrue();
        assertThat(location.get().getId()).isEqualTo(locationId);

    }

}