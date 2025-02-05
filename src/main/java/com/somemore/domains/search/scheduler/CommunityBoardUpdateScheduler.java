package com.somemore.domains.search.scheduler;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.usecase.board.CommunityBoardQueryUseCase;
import com.somemore.domains.search.annotation.ConditionalOnElasticSearchEnabled;
import com.somemore.domains.search.config.ElasticsearchHealthChecker;
import com.somemore.domains.search.usecase.CommunityBoardDocumentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnElasticSearchEnabled
public class CommunityBoardUpdateScheduler {

    private final CommunityBoardQueryUseCase communityBoardQueryUseCase;
    private final CommunityBoardDocumentUseCase communityBoardDocumentUseCase;
    private final ElasticsearchHealthChecker elasticsearchHealthChecker;

    @Scheduled(cron = "${spring.schedules.cron.updateCommunityBoardDocuments}")
    public void updateCommunityBoardDocuments() {
        if (elasticsearchHealthChecker.isElasticsearchRunning()) {
            List<CommunityBoard> communityBoards = communityBoardQueryUseCase.getAllCommunityBoards();
            communityBoardDocumentUseCase.saveCommunityBoardDocuments(communityBoards);
        }
    }
}
