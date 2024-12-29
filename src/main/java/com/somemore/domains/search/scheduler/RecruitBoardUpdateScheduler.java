package com.somemore.domains.search.scheduler;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import com.somemore.domains.search.config.ElasticsearchHealthChecker;
import com.somemore.domains.search.usecase.RecruitBoardDocumentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "elastic.search.enabled", havingValue = "true", matchIfMissing = true)
public class RecruitBoardUpdateScheduler {

    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final RecruitBoardDocumentUseCase recruitBoardDocumentUseCase;
    private final ElasticsearchHealthChecker elasticsearchHealthChecker;

    @Scheduled(cron = "${spring.schedules.cron.updateRecruitBoardDocuments}")
    public void updateRecruitBoardDocuments() {
        List<RecruitBoard> recruitBoards = recruitBoardQueryUseCase.getAllRecruitBoards();

        if (elasticsearchHealthChecker.isElasticsearchRunning()) {
            recruitBoardDocumentUseCase.saveRecruitBoardDocuments(recruitBoards);
        }
    }
}
