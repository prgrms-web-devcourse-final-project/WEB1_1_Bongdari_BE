package com.somemore.domains.volunteerapply.service;

import com.somemore.domains.notification.domain.NotificationSubType;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import com.somemore.domains.volunteer.usecase.UpdateVolunteerUseCase;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.request.VolunteerApplySettleRequestDto;
import com.somemore.domains.volunteerapply.event.VolunteerReviewRequestEvent;
import com.somemore.domains.volunteerapply.usecase.SettleVolunteerApplyFacadeUseCase;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.common.event.ServerEventType;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.*;

@RequiredArgsConstructor
@Transactional
@Service
public class SettleVolunteerApplyFacadeService implements SettleVolunteerApplyFacadeUseCase {

    private final VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;
    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final UpdateVolunteerUseCase updateVolunteerUseCase;
    private final ServerEventPublisher serverEventPublisher;

    @Override
    public void settleVolunteerApplies(VolunteerApplySettleRequestDto dto, UUID centerId) {
        List<VolunteerApply> applies = volunteerApplyQueryUseCase.getAllByIds(dto.ids());
        Long recruitBoardId = applies.getFirst().getRecruitBoardId();
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(recruitBoardId);

        validateVolunteerApplyExistence(dto.ids(), applies);
        validateRecruitBoardConsistency(applies, recruitBoardId);
        validateAuth(recruitBoard, centerId);

        int hours = recruitBoard.getRecruitmentInfo().getVolunteerHours();

        applies.forEach(apply -> {
            apply.changeAttended(true);
            updateVolunteerUseCase.updateVolunteerStats(apply.getVolunteerId(), hours);
            publishVolunteerReviewRequestEvent(apply, recruitBoard);
        });

    }

    private void validateAuth(RecruitBoard recruitBoard, UUID centerId) {
        if (recruitBoard.isAuthor(centerId)) {
            return;
        }
        throw new BadRequestException(UNAUTHORIZED_RECRUIT_BOARD);
    }

    private void validateVolunteerApplyExistence(List<Long> ids, List<VolunteerApply> applies) {
        if (ids.size() == applies.size()) {
            return;
        }
        throw new BadRequestException(VOLUNTEER_APPLY_LIST_MISMATCH);
    }

    private void validateRecruitBoardConsistency(List<VolunteerApply> applies,
                                                 Long recruitBoardId) {
        boolean anyMismatch = applies.stream()
                .anyMatch(apply -> !apply.getRecruitBoardId().equals(recruitBoardId));

        if (anyMismatch) {
            throw new BadRequestException(RECRUIT_BOARD_ID_MISMATCH);
        }
    }

    private void publishVolunteerReviewRequestEvent(VolunteerApply apply, RecruitBoard recruitBoard) {
        VolunteerReviewRequestEvent event = VolunteerReviewRequestEvent.builder()
                .type(ServerEventType.NOTIFICATION)
                .subType(NotificationSubType.VOLUNTEER_REVIEW_REQUEST)
                .volunteerId(apply.getVolunteerId())
                .volunteerApplyId(apply.getId())
                .centerId(recruitBoard.getCenterId())
                .recruitBoardId(recruitBoard.getId())
                .build();

        serverEventPublisher.publish(event);
    }
}
