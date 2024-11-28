package com.somemore.recruitboard.service.command;

import static com.somemore.common.fixture.LocalDateTimeFixture.createCurrentDateTime;
import static com.somemore.common.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.common.fixture.LocalDateTimeFixture.createUpdateStartDateTime;
import static com.somemore.recruitboard.domain.VolunteerType.ADMINISTRATIVE_SUPPORT;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.exception.BadRequestException;
import com.somemore.location.domain.Location;
import com.somemore.location.repository.LocationRepository;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.domain.RecruitmentInfo;
import com.somemore.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.recruitboard.repository.RecruitBoardJpaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UpdateRecruitBoardServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateRecruitBoardService updateRecruitBoardService;

    @Autowired
    private RecruitBoardJpaRepository recruitBoardJpaRepository;

    @Autowired
    private LocationRepository locationRepository;

    private RecruitBoard recruitBoard;
    private UUID centerId;

    @BeforeEach
    void setUp() {
        Location location = createLocation();
        locationRepository.saveAndFlush(location);
        centerId = UUID.randomUUID();
        recruitBoard = createRecruitBoard(centerId, location.getId());
        recruitBoardJpaRepository.saveAndFlush(recruitBoard);
    }

    @AfterEach
    void tearDown() {
        recruitBoardJpaRepository.deleteAllInBatch();
        locationRepository.deleteAllInBatch();
    }

    @DisplayName("봉사 모집글의 데이터를 업데이트하면 저장소에 반영된다.")
    @Test
    void updateRecruitBoard() {
        // given
        LocalDateTime newStartDateTime = createUpdateStartDateTime();
        LocalDateTime newEndDateTime = newStartDateTime.plusHours(3);
        String newImgUrl = "https://image.domain.com/updates";
        RecruitBoardUpdateRequestDto dto = RecruitBoardUpdateRequestDto.builder()
            .title("업데이트 제목")
            .content("업데이트 내용")
            .recruitmentCount(1111)
            .volunteerStartDateTime(newStartDateTime)
            .volunteerEndDateTime(newEndDateTime)
            .volunteerType(ADMINISTRATIVE_SUPPORT)
            .admitted(false)
            .build();

        // when
        updateRecruitBoardService.updateRecruitBoard(dto, recruitBoard.getId(), centerId,
            newImgUrl);

        // then
        RecruitBoard updatedRecruitBoard = recruitBoardJpaRepository.findById(recruitBoard.getId())
            .orElseThrow();

        assertThat(updatedRecruitBoard.getTitle()).isEqualTo(dto.title());
        assertThat(updatedRecruitBoard.getContent()).isEqualTo(dto.content());
        assertThat(updatedRecruitBoard.getImgUrl()).isEqualTo(newImgUrl);

        RecruitmentInfo recruitmentInfo = updatedRecruitBoard.getRecruitmentInfo();
        assertThat(recruitmentInfo.getRecruitmentCount()).isEqualTo(dto.recruitmentCount());
        assertThat(recruitmentInfo.getVolunteerType()).isEqualTo(dto.volunteerType());
        assertThat(recruitmentInfo.getAdmitted()).isEqualTo(dto.admitted());

        assertThat(recruitmentInfo.getVolunteerStartDateTime())
            .isEqualToIgnoringNanos(dto.volunteerStartDateTime());
        assertThat(recruitmentInfo.getVolunteerEndDateTime())
            .isEqualToIgnoringNanos(dto.volunteerEndDateTime());
    }

    @DisplayName("봉사 모집글 위치를 수정할 수 있다")
    @Test
    void updateRecruitBoardLocation() {
        // given
        RecruitBoardLocationUpdateRequestDto dto = RecruitBoardLocationUpdateRequestDto.builder()
            .region("새로새로지역지역")
            .address("새로새로주소주소")
            .latitude(BigDecimal.valueOf(37.2222222))
            .longitude(BigDecimal.valueOf(127.2222222))
            .build();

        // when
        updateRecruitBoardService.updateRecruitBoardLocation(dto, recruitBoard.getId(), centerId);

        // then
        RecruitBoard updateRecruitBoard = recruitBoardJpaRepository.findById(recruitBoard.getId())
            .orElseThrow();
        Location updateLocation = locationRepository.findById(recruitBoard.getLocationId())
            .orElseThrow();

        assertThat(updateRecruitBoard.getRecruitmentInfo().getRegion()).isEqualTo(dto.region());
        assertThat(updateLocation.getAddress()).isEqualTo(dto.address());
        assertThat(updateLocation.getLongitude())
            .isEqualByComparingTo(dto.longitude());
        assertThat(updateLocation.getLatitude())
            .isEqualByComparingTo(dto.latitude());
    }

    @DisplayName("봉사 모집글은 작성자만 수정할 수 있다")
    @Test
    void updateRecruitBoardWhenCenterIdIsWrong() {
        // given
        Long id = recruitBoard.getId();
        UUID wrongCenterId = UUID.randomUUID();
        LocalDateTime newStartDateTime = createUpdateStartDateTime();
        LocalDateTime newEndDateTime = newStartDateTime.plusHours(3);
        String newImgUrl = "https://image.domain.com/updates";
        RecruitBoardUpdateRequestDto dto = RecruitBoardUpdateRequestDto.builder()
            .title("업데이트 제목")
            .content("업데이트 내용")
            .recruitmentCount(1111)
            .volunteerStartDateTime(newStartDateTime)
            .volunteerEndDateTime(newEndDateTime)
            .volunteerType(ADMINISTRATIVE_SUPPORT)
            .admitted(false)
            .build();

        // when
        // then
        Assertions.assertThatThrownBy(
            () -> updateRecruitBoardService.updateRecruitBoard(dto, id, wrongCenterId, newImgUrl)
        ).isInstanceOf(BadRequestException.class);

    }

    @DisplayName("봉사 모집글 상태를 변경할 수 있다")
    @Test
    void updateRecruitBoardStatus() {
        // given
        Long recruitBoardId = recruitBoard.getId();
        RecruitStatus newStatus = RecruitStatus.CLOSED;
        LocalDateTime currentDateTime = createCurrentDateTime();

        // when
        updateRecruitBoardService.updateRecruitBoardStatus(newStatus, recruitBoardId, centerId,
            currentDateTime);

        // then
        RecruitBoard findBoard = recruitBoardJpaRepository.findById(recruitBoardId).orElseThrow();
        assertThat(findBoard.getRecruitStatus()).isEqualTo(newStatus);
    }

    private static RecruitBoard createRecruitBoard(UUID centerId, Long locationId) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        return createRecruitBoard(centerId, locationId, startDateTime, endDateTime);
    }

    private static RecruitBoard createRecruitBoard(UUID centerId, Long locationId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(centerId)
            .locationId(locationId)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }

    private static Location createLocation() {
        return Location.builder()
            .address("주소주소")
            .longitude(BigDecimal.valueOf(37.11111))
            .latitude(BigDecimal.valueOf(127.11111))
            .build();
    }
}
