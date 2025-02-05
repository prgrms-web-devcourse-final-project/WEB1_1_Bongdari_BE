package com.somemore.domains.search.scheduler;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.usecase.RecruitBoardQueryUseCase;
import com.somemore.domains.search.annotation.ConditionalOnElasticSearchEnabled;
import com.somemore.domains.search.config.ElasticsearchHealthChecker;
import com.somemore.domains.search.usecase.RecruitBoardDocumentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnElasticSearchEnabled
public class RecruitBoardUpdateScheduler {

    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final RecruitBoardDocumentUseCase recruitBoardDocumentUseCase;
    private final ElasticsearchHealthChecker elasticsearchHealthChecker;

    @Scheduled(cron = "${spring.schedules.cron.updateRecruitBoardDocuments}")
    public void updateRecruitBoardDocuments() {
        if (elasticsearchHealthChecker.isElasticsearchRunning()) {
            List<RecruitBoard> recruitBoards = recruitBoardQueryUseCase.getAllRecruitBoards();
            recruitBoardDocumentUseCase.saveRecruitBoardDocuments(recruitBoards);
        }
    }
}
