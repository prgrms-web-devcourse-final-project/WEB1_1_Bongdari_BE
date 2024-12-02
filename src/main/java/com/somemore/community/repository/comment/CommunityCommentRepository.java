package com.somemore.community.repository.comment;

import com.somemore.community.domain.CommunityComment;
import com.somemore.community.repository.mapper.CommunityCommentView;

import java.util.List;
import java.util.Optional;

public interface CommunityCommentRepository {
    CommunityComment save(CommunityComment communityComment);
    Optional<CommunityComment> findById(Long id);
    List<CommunityCommentView> findCommentsByBoardId(Long boardId);
    boolean existsById(Long id);
    default boolean doesNotExistById(Long id) {
        return !existsById(id);
    }
    void deleteAllInBatch();
}
