package com.somemore.domains.recruitboard.service.command;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitmentInfo;
import com.somemore.domains.recruitboard.repository.RecruitBoardJpaRepository;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.somemore.domains.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.support.fixture.LocalDateTimeFixture.createStartDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeleteRecruitBoardServiceTest extends IntegrationTestSupport {

    @Autowired
    private DeleteRecruitBoardService deleteRecruitBoardService;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private RecruitBoardJpaRepository recruitBoardJpaRepository;

    private RecruitBoard recruitBoard;

    @BeforeEach
    void setUp() {
        recruitBoard = createRecruitBoard();
        recruitBoardRepository.save(recruitBoard);
    }

    @AfterEach
    void tearDown() {
        recruitBoardJpaRepository.deleteAllInBatch();
    }

    @DisplayName("봉사 모집글 식별값으로 모집글을 삭제할 수 있다")
    @Test
    void deleteRecruitBoard() {
        // given
        UUID centerId = recruitBoard.getCenterId();
        Long recruitBoardId = recruitBoard.getId();

        // when
        deleteRecruitBoardService.deleteRecruitBoard(centerId, recruitBoardId);

        // then
        Optional<RecruitBoard> findBoard = recruitBoardRepository.findById(recruitBoardId);

        assertThat(findBoard).isEmpty();
    }

    @DisplayName("모집글 삭제시 작성자가 아니면 에러가 발생한다")
    @Test
    void deleteRecruitBoardWithWrongCenterId() {
        // given
        UUID wrongCenterId = UUID.randomUUID();
        Long recruitBoardId = recruitBoard.getId();

        // when
        // then
        assertThatThrownBy(
                () -> deleteRecruitBoardService.deleteRecruitBoard(wrongCenterId, recruitBoardId)
        ).isInstanceOf(BadRequestException.class);
    }

    private static RecruitBoard createRecruitBoard() {

        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

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
                .centerId(UUID.randomUUID())
                .locationId(1L)
                .title("봉사모집제목")
                .content("봉사모집내용")
                .imgUrl("https://image.domain.com/links")
                .recruitmentInfo(recruitmentInfo)
                .build();
    }
}
