package com.somemore.domains.recruitboard.domain;

import static com.somemore.domains.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.domains.recruitboard.domain.RecruitStatus.RECRUITING;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.support.fixture.LocalDateTimeFixture.createCurrentDateTime;
import static com.somemore.support.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.support.fixture.LocalDateTimeFixture.createUpdateStartDateTime;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.domains.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RecruitBoardTest {

    private UUID centerId;
    private RecruitBoard board;

    @BeforeEach
    void setUp() {
        centerId = UUID.randomUUID();
        board = createRecruitBoard(centerId);
    }

    @DisplayName("봉사 모집글을 업데이트 할 수 있다")
    @Test
    void updateRecruitBoard() {
        // given
        String imgUrl = "https://image.domain.com/updates";
        LocalDateTime updateStartDateTime = createUpdateStartDateTime();
        LocalDateTime updateEndDateTime = updateStartDateTime.plusHours(2);

        RecruitBoardUpdateRequestDto dto = RecruitBoardUpdateRequestDto.builder()
                .title("봉사 모집글 작성 수정")
                .content("봉사 하실분을 모집합니다. 수정 <br>")
                .recruitmentCount(10)
                .volunteerStartDateTime(updateStartDateTime)
                .volunteerEndDateTime(updateEndDateTime)
                .volunteerHours(2)
                .volunteerCategory(OTHER)
                .admitted(true).build();

        // when
        board.updateWith(dto, imgUrl);

        // then
        assertThat(board.getTitle()).isEqualTo(dto.title());
        assertThat(board.getContent()).isEqualTo(dto.content());
        assertThat(board.getImgUrl()).isEqualTo(imgUrl);
    }

    @DisplayName("봉사 활동 지역을 수정할 수 있다.")
    @Test
    void updateWithRegion() {
        // given
        String updateRegion = "새로운지역";

        // when
        board.updateWith(updateRegion);

        // then
        RecruitmentInfo recruitmentInfo = board.getRecruitmentInfo();
        assertThat(recruitmentInfo.getRegion()).isEqualTo(updateRegion);
    }

    @DisplayName("올바른 기관 식별 값이 주어지면 작성자인지 확인할 수 있다")
    @Test
    void isWriterWithCorrectCenterId() {
        // given
        // when
        boolean isWriter = board.isWriter(centerId);

        // then
        assertThat(isWriter).isTrue();
    }

    @DisplayName("잘못된 기관 식별 값이 주어지면 잘못된 작성자인 확인할 수있다.")
    @Test
    void isNotWriterWithWrongCenterId() {
        // given
        UUID wrongId = UUID.randomUUID();

        // when
        boolean isWriter = board.isWriter(wrongId);

        // then
        assertThat(isWriter).isFalse();
    }

    @DisplayName("모집글 상태를 업데이트 할 수 있다.")
    @Test
    void updateRecruitStatus() {
        // given
        RecruitStatus updateRecruitStatus = CLOSED;

        // when
        board.updateRecruitStatus(updateRecruitStatus);

        // then
        assertThat(board.getRecruitStatus()).isEqualTo(updateRecruitStatus);

    }

    @DisplayName("모집중일 경우 True 반환한다.")
    @Test
    void isRecruitOpen() {
        // given
        // when
        boolean result = board.isRecruiting();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("모집글 수정 가능 여부를 확인할 수 있다.")
    @Test
    void isUpdatable() {
        // given
        // volunteerStartDateTime 내일 13:00:00
        // current 오늘 16:00:00
        LocalDateTime current = createCurrentDateTime();

        // when
        boolean updatable = board.isUpdatable(current);

        // then
        assertThat(updatable).isTrue();
    }

    public static RecruitBoard createRecruitBoard(UUID centerId) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region("지역")
                .recruitmentCount(1)
                .volunteerStartDateTime(startDateTime)
                .volunteerEndDateTime(endDateTime)
                .volunteerHours(1)
                .volunteerCategory(OTHER)
                .admitted(true)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(1L)
                .title("제목")
                .content("내용")
                .recruitmentInfo(recruitmentInfo)
                .status(RECRUITING)
                .imgUrl("이미지 링크")
                .build();
    }
}
