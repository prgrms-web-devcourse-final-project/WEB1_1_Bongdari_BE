package com.somemore.domains.search.scheduler;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.usecase.board.CommunityBoardQueryUseCase;
import com.somemore.domains.search.config.ElasticsearchHealthChecker;
import com.somemore.domains.search.usecase.CommunityBoardDocumentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "elastic.search.enabled", havingValue = "true", matchIfMissing = true)
public class CommunityBoardUpdateScheduler {

    private final CommunityBoardQueryUseCase communityBoardQueryUseCase;
    private final CommunityBoardDocumentUseCase communityBoardDocumentUseCase;
    private final ElasticsearchHealthChecker elasticsearchHealthChecker;

    @Scheduled(cron = "${spring.schedules.cron.updateCommunityBoardDocuments}")
    public void updateCommunityBoardDocuments() {
        List<CommunityBoard> communityBoards = communityBoardQueryUseCase.getAllCommunityBoards();

        if (elasticsearchHealthChecker.isElasticsearchRunning()) {
            communityBoardDocumentUseCase.saveCommunityBoardDocuments(communityBoards);
        }
    }
}