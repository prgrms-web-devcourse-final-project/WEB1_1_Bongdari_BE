package com.somemore.recruitboard.scheduler;

import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.usecase.query.RecruitBoardDocumentUseCase;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecruitBoardScheduler {

    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final RecruitBoardDocumentUseCase recruitBoardDocumentUseCase;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateRecruitBoardDocuments() {
        List<RecruitBoard> recruitBoards = recruitBoardQueryUseCase.getAllRecruitBoards();
        recruitBoardDocumentUseCase.saveRecruitBoardDocuments(recruitBoards);
    }
}
