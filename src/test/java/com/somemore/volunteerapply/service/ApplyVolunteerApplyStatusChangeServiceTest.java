package com.somemore.volunteerapply.service;

import static com.somemore.common.fixture.RecruitBoardFixture.createCloseRecruitBoard;
import static com.somemore.common.fixture.RecruitBoardFixture.createRecruitBoard;
import static com.somemore.global.exception.ExceptionMessage.RECRUITMENT_NOT_OPEN;
import static com.somemore.volunteerapply.domain.ApplyStatus.WAITING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.dto.request.VolunteerApplyCreateRequestDto;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
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
        RecruitBoard board = createRecruitBoard();
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
        RecruitBoard board = createCloseRecruitBoard();
        recruitBoardRepository.save(board);

        VolunteerApplyCreateRequestDto dto = VolunteerApplyCreateRequestDto.builder()
                .recruitBoardId(board.getId())
                .build();

        UUID volunteerId = UUID.randomUUID();

        // when
        // then
        assertThatThrownBy(
                () -> volunteerApplyCommandService.apply(dto, volunteerId)
        ).isInstanceOf(BadRequestException.class)
                .hasMessage(RECRUITMENT_NOT_OPEN.getMessage());
    }

    @DisplayName("중복으로 지원할 수 없다.")
    @Test
    void applyWhenDuplicate() {
        // given
        RecruitBoard board = createCloseRecruitBoard();
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
                () -> volunteerApplyCommandService.apply(dto, volunteerId)
        ).isInstanceOf(BadRequestException.class)
                .hasMessage(RECRUITMENT_NOT_OPEN.getMessage());
    }

}
