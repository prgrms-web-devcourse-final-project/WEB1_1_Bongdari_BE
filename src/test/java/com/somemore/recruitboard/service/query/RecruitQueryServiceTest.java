package com.somemore.recruitboard.service.query;

import static com.somemore.common.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.VolunteerInfo;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RecruitQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private RecruitQueryService recruitQueryService;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    private RecruitBoard recruitBoard;

    @BeforeEach
    void setUp() {
        recruitBoard = createRecruitBoard();
        recruitBoardRepository.saveAndFlush(recruitBoard);
    }

    @AfterEach
    void tearDown() {
        recruitBoardRepository.deleteAllInBatch();
    }

    @DisplayName("존재하는 ID가 주어지면 RecruitBoard 엔티티를 조회할 수 있다")
    @Test
    void findByIdWithExistsId() {
        // given
        Long id = recruitBoard.getId();

        // when
        Optional<RecruitBoard> findBoard = recruitQueryService.findById(id);

        // then
        assertThat(findBoard).isPresent();
    }

    @DisplayName("존재하지 않는 ID가 주어지면 빈 Optional 반환한다.")
    @Test
    void findByIdWithDoesNotExistId() {
        // given
        Long wrongId = 999L;

        // when
        Optional<RecruitBoard> findBoard = recruitQueryService.findById(wrongId);

        // then
        assertThat(findBoard).isEmpty();
    }

    @DisplayName("ID로 RecruitBoard 조회할 수 있다")
    @Test
    void findByIdOrThrowWithExistsId() {
        // given
        Long id = recruitBoard.getId();

        // when
        RecruitBoard findBoard = recruitQueryService.findByIdOrThrow(id);

        // then
        assertThat(findBoard.getId()).isEqualTo(id);
    }

    @DisplayName("존재하지 않는 ID로 RecruitBoard 조회하면 에러가 발생한다")
    @Test
    void findByIdOrThrowWithDoesNotExistId() {
        // given
        Long wrongId = 999L;

        // when
        // then
        assertThatThrownBy(
            () -> recruitQueryService.findByIdOrThrow(wrongId)
        ).isInstanceOf(BadRequestException.class);

    }

    private static RecruitBoard createRecruitBoard() {

        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        VolunteerInfo volunteerInfo = VolunteerInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(UUID.randomUUID())
            .locationId(1L)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .volunteerInfo(volunteerInfo)
            .build();
    }
}