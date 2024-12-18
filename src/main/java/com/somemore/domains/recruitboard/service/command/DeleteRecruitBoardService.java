package com.somemore.domains.recruitboard.service.command;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_RECRUIT_BOARD;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;

import com.somemore.global.exception.BadRequestException;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.recruitboard.usecase.command.DeleteRecruitBoardUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class DeleteRecruitBoardService implements DeleteRecruitBoardUseCase {

    private final RecruitBoardRepository recruitBoardRepository;

    @Override
    public void deleteRecruitBoard(UUID centerId, Long recruitBoardId) {
        RecruitBoard recruitBoard = recruitBoardRepository.findById(recruitBoardId).orElseThrow(
            () -> new BadRequestException(NOT_EXISTS_RECRUIT_BOARD.getMessage())
        );

        validateWriter(recruitBoard, centerId);

        recruitBoard.markAsDeleted();
        recruitBoardRepository.save(recruitBoard);
    }

    private void validateWriter(RecruitBoard recruitBoard, UUID centerId) {
        if (recruitBoard.isWriter(centerId)) {
            return;
        }

        throw new BadRequestException(UNAUTHORIZED_RECRUIT_BOARD.getMessage());
    }
}
