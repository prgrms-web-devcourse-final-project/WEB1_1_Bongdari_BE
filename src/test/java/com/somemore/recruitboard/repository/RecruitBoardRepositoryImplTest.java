package com.somemore.recruitboard.repository;

import static com.somemore.common.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.RecruitmentInfo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RecruitBoardRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private RecruitBoardRepositoryImpl recruitBoardRepository;

    @Autowired
    private RecruitBoardJpaRepository recruitBoardJpaRepository;

    private RecruitBoard deletedRecruitBoard;

    @BeforeEach
    void setUp() {
        deletedRecruitBoard = createRecruitBoard();
        recruitBoardRepository.save(deletedRecruitBoard);
        deletedRecruitBoard.markAsDeleted();
        recruitBoardRepository.save(deletedRecruitBoard);
    }

    @AfterEach
    void tearDown() {
        recruitBoardJpaRepository.deleteAllInBatch();
    }

    @DisplayName("논리 삭제된 데이터를 id로 조회시 빈 Optional 반환된다")
    @Test
    void findById() {
        // given
        Long deletedId = deletedRecruitBoard.getId();

        // when
        Optional<RecruitBoard> findBoard = recruitBoardRepository.findById(deletedId);

        // then
        assertThat(findBoard).isEmpty();
    }

    @DisplayName("논리 삭제된 데이터는 findAll()로 조회하지 않는다.")
    @Test
    void logicallyDeletedDataIsNotFetchedByGetAll() {
        // given
        RecruitBoard notDeletedRecruitBoard = createRecruitBoard();
        recruitBoardRepository.save(notDeletedRecruitBoard);

        // when
        List<RecruitBoard> allBoards = recruitBoardRepository.findAll();

        // then
        assertThat(allBoards).isNotEmpty();
        assertThat(allBoards).hasSize(1);
        assertThat(allBoards.getFirst().getId()).isEqualTo(notDeletedRecruitBoard.getId());
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
