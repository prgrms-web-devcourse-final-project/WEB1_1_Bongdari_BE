package com.somemore.volunteerapply.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER_APPLY;
import static com.somemore.global.exception.ExceptionMessage.RECRUIT_BOARD_ALREADY_COMPLETED;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;
import static com.somemore.volunteerapply.domain.ApplyStatus.REJECTED;

import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.volunteerapply.usecase.RejectVolunteerApplyUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RejectVolunteerApplyService implements RejectVolunteerApplyUseCase {

    private final VolunteerApplyRepository volunteerApplyRepository;
    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;

    @Override
    public void reject(Long id, UUID centerId) {
        VolunteerApply apply = getApply(id);
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(apply.getRecruitBoardId());

        validateWriter(recruitBoard, centerId);
        validateBoardStatus(recruitBoard);

        apply.changeStatus(REJECTED);
        volunteerApplyRepository.save(apply);
    }

    private VolunteerApply getApply(Long id) {
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

}
