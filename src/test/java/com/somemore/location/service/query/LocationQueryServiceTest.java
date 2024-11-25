package com.somemore.location.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.exception.BadRequestException;
import com.somemore.location.domain.Location;
import com.somemore.location.repository.LocationRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LocationQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private LocationQueryService locationQueryService;

    @Autowired
    private LocationRepository locationRepository;

    private Location location;

    @BeforeEach
    void setUp() {
        location = Location.builder()
            .address("123")
            .latitude(BigDecimal.valueOf(37.0))
            .longitude(BigDecimal.valueOf(127.0))
            .build();
        locationRepository.saveAndFlush(location);
    }

    @AfterEach
    void tearDown() {
        locationRepository.deleteAllInBatch();
    }

    @DisplayName("존재하는 ID가 주어지면 Location 엔티티를 조회할 수 있다")
    @Test
    void findByIdWithExistsId() {
        // given
        Long id = location.getId();

        // when
        Optional<Location> findLocation = locationQueryService.findById(id);

        // then
        assertThat(findLocation).isPresent();
    }

    @DisplayName("존재하지 않는 ID가 주어지면 빈 Optional 반환한다.")
    @Test
    void findByIdWithDoesNotExistId() {
        // given
        Long wrongId = 999L;

        // when
        Optional<Location> findLocation = locationQueryService.findById(wrongId);

        // then
        assertThat(findLocation).isEmpty();
    }

    @DisplayName("ID로 Location 조회할 수 있다")
    @Test
    void findByIdOrThrowWithExistsId() {
        // given
        Long id = location.getId();

        // when
        Location findLocation = locationQueryService.findByIdOrThrow(id);

        // then
        assertThat(findLocation.getId()).isEqualTo(id);
    }

    @DisplayName("존재하지 않는 ID로 Location 조회하면 에러가 발생한다")
    @Test
    void findByIdOrThrowWithDoesNotExistId() {
        // given
        Long wrongId = 999L;

        // when
        // then
        assertThatThrownBy(
            () -> locationQueryService.findByIdOrThrow(wrongId)
        ).isInstanceOf(BadRequestException.class);
    }
}
