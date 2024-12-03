package com.somemore.volunteerapply.usecase;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER_APPLY;
import static com.somemore.global.exception.ExceptionMessage.RECRUIT_BOARD_ALREADY_COMPLETED;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;
import static com.somemore.volunteerapply.domain.ApplyStatus.APPROVED;

import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ApproveVolunteerApplyService implements ApproveVolunteerApplyUseCase {

    private final VolunteerApplyRepository volunteerApplyRepository;

    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;

    @Override
    public void approve(Long id, UUID centerId) {
        VolunteerApply apply = getVolunteerApply(id);
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(apply.getRecruitBoardId());

        validateApprovalConditions(recruitBoard, centerId);
        validateBoardStatus(recruitBoard);

        apply.changeStatus(APPROVED);
        volunteerApplyRepository.save(apply);
    }

    private VolunteerApply getVolunteerApply(Long id) {
        return volunteerApplyRepository.findById(id).orElseThrow(
                () -> new BadRequestException(NOT_EXISTS_VOLUNTEER_APPLY)
        );
    }

    private void validateApprovalConditions(RecruitBoard recruitBoard, UUID centerId) {
        if (!recruitBoard.isWriter(centerId)) {
            throw new BadRequestException(UNAUTHORIZED_RECRUIT_BOARD);
        }
    }

    private void validateBoardStatus(RecruitBoard recruitBoard) {
        if (recruitBoard.isCompleted()) {
            throw new BadRequestException(RECRUIT_BOARD_ALREADY_COMPLETED);
        }
    }
}
