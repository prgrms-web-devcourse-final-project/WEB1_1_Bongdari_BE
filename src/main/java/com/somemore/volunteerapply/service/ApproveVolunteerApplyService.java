package com.somemore.volunteerapply.service;

import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.common.event.ServerEventType;
import com.somemore.global.exception.BadRequestException;
import com.somemore.notification.domain.NotificationSubType;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.event.VolunteerApplyStatusChangeEvent;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.volunteerapply.usecase.ApproveVolunteerApplyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER_APPLY;
import static com.somemore.global.exception.ExceptionMessage.RECRUIT_BOARD_ALREADY_COMPLETED;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;
import static com.somemore.volunteerapply.domain.ApplyStatus.APPROVED;

@RequiredArgsConstructor
@Transactional
@Service
public class ApproveVolunteerApplyService implements ApproveVolunteerApplyUseCase {

    private final VolunteerApplyRepository volunteerApplyRepository;
    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final ServerEventPublisher serverEventPublisher;

    @Override
    public void approve(Long id, UUID centerId) {
        VolunteerApply apply = getVolunteerApply(id);
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(apply.getRecruitBoardId());

        validateWriter(recruitBoard, centerId);
        validateBoardStatus(recruitBoard);

        apply.changeStatus(APPROVED);
        volunteerApplyRepository.save(apply);

        publishVolunteerApplyStatusChangeEvent(apply.getVolunteerId(), id, recruitBoard, apply);
    }

    private VolunteerApply getVolunteerApply(Long id) {
        return volunteerApplyRepository.findById(id).orElseThrow(
                () -> new BadRequestException(NOT_EXISTS_VOLUNTEER_APPLY)
        );
    }

    private void validateWriter(RecruitBoard recruitBoard, UUID centerId) {
        if (recruitBoard.isWriter(centerId)) {
            return;
        }
        throw new BadRequestException(UNAUTHORIZED_RECRUIT_BOARD);
    }

    private void validateBoardStatus(RecruitBoard recruitBoard) {
        if (recruitBoard.isCompleted()) {
            throw new BadRequestException(RECRUIT_BOARD_ALREADY_COMPLETED);
        }
    }

    private void publishVolunteerApplyStatusChangeEvent(UUID receiverId, Long id, RecruitBoard recruitBoard, VolunteerApply apply) {
        VolunteerApplyStatusChangeEvent event = VolunteerApplyStatusChangeEvent.builder()
                .type(ServerEventType.NOTIFICATION)
                .subType(NotificationSubType.VOLUNTEER_APPLY_STATUS_CHANGE)
                .receiverId(receiverId)
                .volunteerApplyId(id)
                .centerId(recruitBoard.getCenterId())
                .recruitBoardId(recruitBoard.getId())
                .newStatus(apply.getStatus())
                .build();

        serverEventPublisher.publish(event);
    }
}
