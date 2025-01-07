package com.somemore.domains.recruitboard.service;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.service.validator.RecruitBoardValidator;
import com.somemore.domains.recruitboard.usecase.DeleteRecruitBoardUseCase;
import com.somemore.domains.recruitboard.usecase.RecruitBoardQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class DeleteRecruitBoardService implements DeleteRecruitBoardUseCase {

    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final RecruitBoardValidator recruitBoardValidator;

    @Override
    public void deleteRecruitBoard(UUID centerId, Long id) {
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(id);
        recruitBoardValidator.validateWriter(recruitBoard, centerId);

        recruitBoard.markAsDeleted();
    }

}
