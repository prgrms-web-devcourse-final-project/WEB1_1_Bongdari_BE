package com.somemore.community.scheduler;

import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.usecase.board.CommunityBoardDocumentUseCase;
import com.somemore.community.usecase.board.CommunityBoardQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommunityBoardUpdateScheduler {

    private final CommunityBoardQueryUseCase communityBoardQueryUseCase;
    private final CommunityBoardDocumentUseCase communityBoardDocumentUseCase;


    @Scheduled(cron = "${spring.schedules.cron.updateCommunityBoardDocuments}")
    public void updateCommunityBoardDocuments() {
        List<CommunityBoard> communityBoards = communityBoardQueryUseCase.getAllCommunityBoards();
        communityBoardDocumentUseCase.saveCommunityBoardDocuments(communityBoards);
    }
}
