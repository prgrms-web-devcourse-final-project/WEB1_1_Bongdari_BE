package com.somemore.domains.volunteerapply.service;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.usecase.RecruitBoardQueryUseCase;
import com.somemore.domains.volunteerapply.domain.ApplyStatus;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.event.VolunteerApplyStatusChangeEvent;
import com.somemore.domains.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.domains.volunteerapply.usecase.ApproveVolunteerApplyUseCase;
import com.somemore.domains.volunteerapply.usecase.RejectVolunteerApplyUseCase;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.domains.volunteerapply.domain.ApplyStatus.APPROVED;
import static com.somemore.domains.volunteerapply.domain.ApplyStatus.REJECTED;
import static com.somemore.global.exception.ExceptionMessage.*;

@RequiredArgsConstructor
@Transactional
@Service
public class VolunteerApplyStatusChangeService implements ApproveVolunteerApplyUseCase, RejectVolunteerApplyUseCase {

    private final VolunteerApplyRepository volunteerApplyRepository;
    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final ServerEventPublisher serverEventPublisher;

    @Override
    public void approve(Long id, UUID centerId) {
        changeApplyStatus(id, centerId, APPROVED);
    }

    @Override
    public void reject(Long id, UUID centerId) {
        changeApplyStatus(id, centerId, REJECTED);
    }

    private void changeApplyStatus(Long id, UUID centerId, ApplyStatus newStatus) {
        VolunteerApply apply = getVolunteerApply(id);
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(apply.getRecruitBoardId());

        validateWriter(recruitBoard, centerId);
        validateBoardStatus(recruitBoard);

        ApplyStatus oldStatus = apply.getStatus();
        apply.changeStatus(newStatus);

        publishVolunteerApplyStatusChangeEvent(apply, recruitBoard, oldStatus);
    }

    private VolunteerApply getVolunteerApply(Long id) {
        return volunteerApplyRepository.findById(id).orElseThrow(
                () -> new BadRequestException(NOT_EXISTS_VOLUNTEER_APPLY)
        );
    }

    private void validateWriter(RecruitBoard recruitBoard, UUID centerId) {
        if (recruitBoard.isAuthor(centerId)) {
            return;
        }
        throw new BadRequestException(UNAUTHORIZED_RECRUIT_BOARD);
    }

    private void validateBoardStatus(RecruitBoard recruitBoard) {
        if (recruitBoard.isCompleted()) {
            throw new BadRequestException(RECRUIT_BOARD_ALREADY_COMPLETED);
        }
    }

    private void publishVolunteerApplyStatusChangeEvent(VolunteerApply apply,
                                                        RecruitBoard recruitBoard,
                                                        ApplyStatus oldStatus) {
        if (apply.getStatus() == oldStatus) {
            return;
        }
        serverEventPublisher.publish(VolunteerApplyStatusChangeEvent.from(apply, recruitBoard, oldStatus));
    }
}
