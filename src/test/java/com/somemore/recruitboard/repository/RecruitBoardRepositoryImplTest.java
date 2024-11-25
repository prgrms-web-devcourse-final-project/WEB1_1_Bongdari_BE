package com.somemore.recruitboard.repository;

import static com.somemore.common.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.common.fixture.LocalDateTimeFixture;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.VolunteerInfo;
import java.time.LocalDateTime;
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

    private RecruitBoard recruitBoard;
    @BeforeEach
    void setUp() {
        recruitBoard = createRecruitBoard();
        recruitBoardRepository.saveAndFlush(recruitBoard);
        recruitBoard.markAsDeleted();
        recruitBoardRepository.saveAndFlush(recruitBoard);
    }

    @AfterEach
    void tearDown() {
        recruitBoardRepository.deleteAllInBatch();
    }

    @DisplayName("논리 삭제된 데이터를 id로 조회시 빈 Optional 반환된다")
    @Test
    void findById() {
        // given
        Long deletedId = recruitBoard.getId();

        // when
        Optional<RecruitBoard> findBoard = recruitBoardRepository.findById(deletedId);

        // then
        assertThat(findBoard).isEmpty();
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