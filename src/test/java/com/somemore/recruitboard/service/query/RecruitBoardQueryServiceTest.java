package com.somemore.recruitboard.service.query;

import static com.somemore.common.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_RECRUIT_BOARD;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.RecruitmentInfo;
import com.somemore.recruitboard.dto.response.RecruitBoardResponseDto;
import com.somemore.recruitboard.repository.RecruitBoardJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RecruitBoardQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private RecruitBoardQueryService recruitBoardQueryService;

    @Autowired
    private RecruitBoardJpaRepository recruitBoardJpaRepository;

    private RecruitBoard recruitBoard;

    @BeforeEach
    void setUp() {
        recruitBoard = createRecruitBoard();
        recruitBoardJpaRepository.saveAndFlush(recruitBoard);
    }

    @AfterEach
    void tearDown() {
        recruitBoardJpaRepository.deleteAllInBatch();
    }

    @DisplayName("존재하는 ID가 주어지면 RecruitBoard 엔티티를 조회할 수 있다")
    @Test
    void getByIdWithExistsId() {
        // given
        Long id = recruitBoard.getId();

        // when
        RecruitBoardResponseDto dto = recruitBoardQueryService.getById(id);

        // then
        assertThat(dto.id()).isEqualTo(recruitBoard.getId());
    }

    @DisplayName("존재하지 않는 ID가 주어지면 에러가 발생한다.")
    @Test
    void getByIdWithDoesNotExistId() {
        // given
        Long wrongId = 999L;

        // when
        // then
        assertThatThrownBy(
            () -> recruitBoardQueryService.getById(wrongId)
        ).isInstanceOf(BadRequestException.class)
            .hasMessage(NOT_EXISTS_RECRUIT_BOARD.getMessage());
    }

    @DisplayName("모든 데이터를 조회할 수 있다")
    @Test
    void getAll() {
        // given
        RecruitBoard board = createRecruitBoard();
        recruitBoardJpaRepository.saveAndFlush(board);
        board.markAsDeleted();
        recruitBoardJpaRepository.saveAndFlush(board);

        // when
        List<RecruitBoardResponseDto> responseDtos = recruitBoardQueryService.getAll();

        // then
        assertThat(responseDtos).hasSize(1);
        assertThat(responseDtos.getFirst().id()).isEqualTo(recruitBoard.getId());
    }

    private static RecruitBoard createRecruitBoard() {

        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
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
            .recruitmentInfo(recruitmentInfo)
            .build();
    }
}
