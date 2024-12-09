package com.somemore.volunteerapply.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.event.VolunteerApplyStatusChangeEvent;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.common.fixture.RecruitBoardFixture.createCompletedRecruitBoard;
import static com.somemore.common.fixture.RecruitBoardFixture.createRecruitBoard;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;
import static com.somemore.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.volunteerapply.domain.ApplyStatus.APPROVED;
import static com.somemore.volunteerapply.domain.ApplyStatus.REJECTED;
import static com.somemore.volunteerapply.domain.ApplyStatus.WAITING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Transactional
class RejectVolunteerApplyServiceTest extends IntegrationTestSupport {

    private RejectVolunteerApplyService rejectVolunteerApplyService;

    @Autowired
    private VolunteerApplyRepository volunteerApplyRepository;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private RecruitBoardQueryUseCase recruitBoardQueryUseCase;


    ServerEventPublisher serverEventPublisher;

    @BeforeEach
    void setUp() {
        serverEventPublisher = mock(ServerEventPublisher.class);
        rejectVolunteerApplyService = new RejectVolunteerApplyService(
                volunteerApplyRepository,
                recruitBoardQueryUseCase,
                serverEventPublisher
        );
    }

    @DisplayName("봉사 지원을 거절할 수 있다.")
    @Test
    void reject() {
        // given
        UUID centerId = UUID.randomUUID();

        RecruitBoard board = createRecruitBoard(centerId);
        recruitBoardRepository.save(board);

        VolunteerApply apply = createApply(board.getId());
        volunteerApplyRepository.save(apply);

        // when
        rejectVolunteerApplyService.reject(apply.getId(), centerId);

        // then
        VolunteerApply approve = volunteerApplyRepository.findById(apply.getId()).orElseThrow();

        assertThat(approve.getStatus()).isEqualTo(REJECTED);
    }

    @DisplayName("자신이 작성한 모집글이 아닌 신청에 대해 거절할 경우 에러가 발생한다.")
    @Test
    void rejectWithWrongCenter() {
        // given
        RecruitBoard board = createRecruitBoard(UUID.randomUUID());
        recruitBoardRepository.save(board);

        VolunteerApply apply = createApply(board.getId());
        volunteerApplyRepository.save(apply);

        Long id = apply.getId();
        UUID wrongCenterId = UUID.randomUUID();

        // when
        // then
        assertThatThrownBy(
                () -> rejectVolunteerApplyService.reject(id, wrongCenterId)
        ).isInstanceOf(BadRequestException.class)
                .hasMessage(UNAUTHORIZED_RECRUIT_BOARD.getMessage());
    }

    @DisplayName("이미 완료된 봉사 모집에 대해 지원을 거절할 경우 에러가 발생 한다.")
    @Test
    void rejectWithAlreadyCompletedRecruit() {
        // given
        UUID centerId = UUID.randomUUID();
        RecruitBoard board = createCompletedRecruitBoard(centerId, OTHER);
        recruitBoardRepository.save(board);

        VolunteerApply apply = createApply(board.getId());
        volunteerApplyRepository.save(apply);

        Long id = apply.getId();
        // when
        // then
        assertThatThrownBy(
                () -> rejectVolunteerApplyService.reject(id, centerId)
        ).isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.RECRUIT_BOARD_ALREADY_COMPLETED.getMessage());
    }

    @DisplayName("지원 상태가 변경되지 않은 경우 이벤트 퍼블리셔가 호출되지 않는다.")
    @Test
    void approveWithSameStatusDoesNotPublishEvent() {
        // given
        UUID centerId = UUID.randomUUID();

        RecruitBoard board = createRecruitBoard(centerId);
        recruitBoardRepository.save(board);

        VolunteerApply apply = createApply(board.getId());
        apply.changeStatus(REJECTED);
        volunteerApplyRepository.save(apply);

        // when
        rejectVolunteerApplyService.reject(apply.getId(), centerId);

        // then
        verify(serverEventPublisher, never()).publish(any());
        VolunteerApply approvedApply = volunteerApplyRepository.findById(apply.getId()).orElseThrow();
        assertThat(approvedApply.getStatus()).isEqualTo(REJECTED);
    }

    @DisplayName("지원 상태가 변경된 경우 이벤트 퍼블리셔가 호출된다.")
    @Test
    void approveWithDifferentStatusPublishesEvent() {
        // given
        UUID centerId = UUID.randomUUID();

        RecruitBoard board = createRecruitBoard(centerId);
        recruitBoardRepository.save(board);

        VolunteerApply apply = createApply(board.getId());
        volunteerApplyRepository.save(apply);

        // when
        rejectVolunteerApplyService.reject(apply.getId(), centerId);

        // then
        ArgumentCaptor<VolunteerApplyStatusChangeEvent> eventCaptor = ArgumentCaptor.forClass(VolunteerApplyStatusChangeEvent.class);
        verify(serverEventPublisher, times(1)).publish(eventCaptor.capture());

        VolunteerApplyStatusChangeEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent).isNotNull();
        assertThat(capturedEvent.getVolunteerApplyId()).isEqualTo(apply.getId());
    }

    private VolunteerApply createApply(Long recruitBoardId) {
        return VolunteerApply.builder()
                .volunteerId(UUID.randomUUID())
                .recruitBoardId(recruitBoardId)
                .status(WAITING)
                .attended(false)
                .build();
    }
}
