package com.somemore.domains.volunteerapply.service;

import static com.somemore.domains.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.domains.recruitboard.domain.RecruitStatus.RECRUITING;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.domains.volunteerapply.domain.ApplyStatus.WAITING;
import static com.somemore.global.exception.ExceptionMessage.DUPLICATE_APPLICATION;
import static com.somemore.global.exception.ExceptionMessage.RECRUITMENT_NOT_OPEN;
import static com.somemore.support.fixture.LocalDateTimeFixture.createStartDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.domain.RecruitmentInfo;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.request.VolunteerApplyCreateRequestDto;
import com.somemore.domains.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ApplyVolunteerApplyStatusChangeServiceTest extends IntegrationTestSupport {

    @Autowired
    private ApplyVolunteerApplyService volunteerApplyCommandService;

    @Autowired
    private VolunteerApplyRepository volunteerApplyRepository;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @DisplayName("모집중인 봉사 모집글에 지원할 수 있다")
    @Test
    void apply() {
        // given
        RecruitBoard board = createRecruitBoard(RECRUITING);
        recruitBoardRepository.save(board);

        VolunteerApplyCreateRequestDto dto = VolunteerApplyCreateRequestDto.builder()
                .recruitBoardId(board.getId())
                .build();

        UUID volunteerId = UUID.randomUUID();

        // when
        Long applyId = volunteerApplyCommandService.apply(dto, volunteerId);

        // then
        Optional<VolunteerApply> apply = volunteerApplyRepository.findById(applyId);

        assertThat(apply).isPresent();
        assertThat(apply.get().getRecruitBoardId()).isEqualTo(board.getId());
    }

    @DisplayName("모집중이지 않는 봉사 모집글에 신청하면 에러가 발생한다.")
    @Test
    void applyWhenCLOSE() {
        // given
        RecruitBoard board = createRecruitBoard(CLOSED);
        recruitBoardRepository.save(board);

        VolunteerApplyCreateRequestDto dto = VolunteerApplyCreateRequestDto.builder()
                .recruitBoardId(board.getId())
                .build();

        UUID volunteerId = UUID.randomUUID();

        // when
        // then
        assertThatThrownBy(
                () -> volunteerApplyCommandService.apply(dto, volunteerId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(RECRUITMENT_NOT_OPEN.getMessage());
    }

    @DisplayName("중복으로 지원할 수 없다.")
    @Test
    void applyWhenDuplicate() {
        // given
        RecruitBoard board = createRecruitBoard(RECRUITING);
        recruitBoardRepository.save(board);

        UUID volunteerId = UUID.randomUUID();
        VolunteerApply apply = VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(board.getId())
                .status(WAITING)
                .attended(false)
                .build();
        volunteerApplyRepository.save(apply);

        VolunteerApplyCreateRequestDto dto = VolunteerApplyCreateRequestDto.builder()
                .recruitBoardId(board.getId())
                .build();

        // when
        // then
        assertThatThrownBy(
                () -> volunteerApplyCommandService.apply(dto, volunteerId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(DUPLICATE_APPLICATION.getMessage());
    }

    private static RecruitBoard createRecruitBoard(RecruitStatus status) {

        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(2);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region("지역")
                .recruitmentCount(1)
                .volunteerStartDateTime(startDateTime)
                .volunteerEndDateTime(endDateTime)
                .volunteerHours(2)
                .volunteerCategory(OTHER)
                .admitted(true)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(1L)
                .title("제목")
                .content("내용")
                .recruitmentInfo(recruitmentInfo)
                .status(status)
                .build();
    }

}
