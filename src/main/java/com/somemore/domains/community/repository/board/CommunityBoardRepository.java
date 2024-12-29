package com.somemore.domains.community.repository.board;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.repository.mapper.CommunityBoardView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommunityBoardRepository {
    CommunityBoard save(CommunityBoard communityBoard);
    Optional<CommunityBoard> findById(Long id);
    Page<CommunityBoardView> findCommunityBoards(String keyword, Pageable pageable);
    Page<CommunityBoardView> findByWriterId(UUID writerId, Pageable pageable);
    boolean existsById(Long id);
    default boolean doesNotExistById(Long id) {
        return !existsById(id);
    }
    void deleteAllInBatch();
    List<CommunityBoard> findAll();
}
