package com.somemore.community.repository;

import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.domain.CommunityBoardView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommunityBoardRepository {
    CommunityBoard save(CommunityBoard communityBoard);
    Optional<CommunityBoard> findById(Long id);
    List<CommunityBoardView> getCommunityBoards();
    List<CommunityBoardView> findByWriterId(UUID writerId);
    void deleteAllInBatch();
}
