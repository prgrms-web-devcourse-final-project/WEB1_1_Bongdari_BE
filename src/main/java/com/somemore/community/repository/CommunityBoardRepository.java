package com.somemore.community.repository;

import com.somemore.community.domain.CommunityBoard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommunityBoardRepository {
    CommunityBoard save(CommunityBoard communityBoard);
    Optional<CommunityBoard> getCommunityBoardWithId(Long id);
    List<CommunityBoard> getCommunityBoards();
    List<CommunityBoard> getCommunityBoardsByWriterId(UUID writerId);
    void deleteAllInBatch();
}
