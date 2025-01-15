package com.somemore.domains.recruitboard.service;

import static com.somemore.domains.recruitboard.domain.RecruitStatus.RECRUITING;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.ADMINISTRATIVE_SUPPORT;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.support.fixture.LocalDateTimeFixture.createCurrentDateTime;
import static com.somemore.support.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.support.fixture.LocalDateTimeFixture.createUpdateStartDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import com.somemore.domains.location.domain.Location;
import com.somemore.domains.location.repository.LocationRepository;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.domain.RecruitmentInfo;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.domains.recruitboard.repository.RecruitBoardJpaRepository;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.support.IntegrationTestSupport;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class UpdateRecruitBoardServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateRecruitBoardService updateRecruitBoardService;

    @Autowired
    private RecruitBoardJpaRepository recruitBoardJpaRepository;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private LocationRepository locationRepository;

    @SpyBean
    private Clock clock;

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

    @DisplayName("봉사 모집글의 데이터를 업데이트하면 저장소에 반영된다.")
    @Test
    void updateRecruitBoard() {
        // given
        LocalDateTime current = createCurrentDateTime();
        LocalDateTime newStartDateTime = createUpdateStartDateTime();
        LocalDateTime newEndDateTime = newStartDateTime.plusHours(3);
        String newImgUrl = "https://image.domain.com/updates";
        RecruitBoardUpdateRequestDto dto = RecruitBoardUpdateRequestDto.builder()
                .title("업데이트 제목")
                .content("업데이트 내용")
                .recruitmentCount(1111)
                .region("서울특별시")
                .volunteerStartDateTime(newStartDateTime)
                .volunteerEndDateTime(newEndDateTime)
                .volunteerHours(3)
                .volunteerCategory(ADMINISTRATIVE_SUPPORT)
                .admitted(false)
                .build();

        setMockClock(current);

        // when
        updateRecruitBoardService.updateRecruitBoard(dto, recruitBoard.getId(), centerId,
                newImgUrl);

        // then
        RecruitBoard updatedRecruitBoard = recruitBoardRepository.findById(recruitBoard.getId())
                .orElseThrow();

        assertThat(updatedRecruitBoard.getTitle()).isEqualTo(dto.title());
        assertThat(updatedRecruitBoard.getContent()).isEqualTo(dto.content());
        assertThat(updatedRecruitBoard.getImgUrl()).isEqualTo(newImgUrl);

        RecruitmentInfo recruitmentInfo = updatedRecruitBoard.getRecruitmentInfo();
        assertThat(recruitmentInfo.getRecruitmentCount()).isEqualTo(dto.recruitmentCount());
        assertThat(recruitmentInfo.getVolunteerCategory()).isEqualTo(dto.volunteerCategory());
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
        LocalDateTime current = createCurrentDateTime();
        RecruitBoardLocationUpdateRequestDto dto = RecruitBoardLocationUpdateRequestDto.builder()
                .region("새로새로지역지역")
                .address("새로새로주소주소")
                .latitude(BigDecimal.valueOf(37.2222222))
                .longitude(BigDecimal.valueOf(127.2222222))
                .build();

        setMockClock(current);

        // when
        updateRecruitBoardService.updateRecruitBoardLocation(dto, recruitBoard.getId(), centerId);

        // then
        RecruitBoard updateRecruitBoard = recruitBoardRepository.findById(recruitBoard.getId())
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

    @DisplayName("봉사 모집글 상태를 변경할 수 있다")
    @Test
    void updateRecruitBoardStatus() {
        // given
        Long recruitBoardId = recruitBoard.getId();
        RecruitStatus newStatus = RecruitStatus.CLOSED;
        LocalDateTime current = createCurrentDateTime();

        setMockClock(current);

        // when
        updateRecruitBoardService.updateRecruitBoardStatus(newStatus, recruitBoardId, centerId);

        // then
        RecruitBoard findBoard = recruitBoardRepository.findById(recruitBoardId).orElseThrow();
        assertThat(findBoard.getRecruitStatus()).isEqualTo(newStatus);
    }

    private static RecruitBoard createRecruitBoard(UUID centerId, Long locationId) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        return createRecruitBoard(centerId, locationId, startDateTime, endDateTime);
    }

    private static RecruitBoard createRecruitBoard(UUID centerId, Long locationId,
            LocalDateTime startDateTime, LocalDateTime endDateTime) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region("경기")
                .recruitmentCount(1)
                .volunteerStartDateTime(startDateTime)
                .volunteerEndDateTime(endDateTime)
                .volunteerHours(1)
                .volunteerCategory(OTHER)
                .admitted(true)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(locationId)
                .title("봉사모집제목")
                .content("봉사모집내용")
                .imgUrl("https://image.domain.com/links")
                .recruitmentInfo(recruitmentInfo)
                .status(RECRUITING)
                .build();
    }

    private static Location createLocation() {
        return Location.builder()
                .address("주소주소")
                .longitude(BigDecimal.valueOf(37.11111))
                .latitude(BigDecimal.valueOf(127.11111))
                .build();
    }

    private void setMockClock(LocalDateTime current) {
        doReturn(current.atZone(ZoneId.systemDefault()).toInstant())
                .when(clock)
                .instant();
    }
}
