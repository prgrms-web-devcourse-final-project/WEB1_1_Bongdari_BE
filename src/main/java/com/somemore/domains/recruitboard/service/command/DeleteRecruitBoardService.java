package com.somemore.domains.recruitboard.service.command;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.recruitboard.service.validator.RecruitBoardValidator;
import com.somemore.domains.recruitboard.usecase.command.DeleteRecruitBoardUseCase;
import com.somemore.domains.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_RECRUIT_BOARD;

@RequiredArgsConstructor
@Transactional
@Service
public class DeleteRecruitBoardService implements DeleteRecruitBoardUseCase {

    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final RecruitBoardValidator recruitBoardValidator;

    @Override
    public void deleteRecruitBoard(UUID centerId, Long id) {
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(id);
        recruitBoardValidator.validateAuthor(recruitBoard, centerId);

        recruitBoard.markAsDeleted();
    }

}
