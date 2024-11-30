package com.somemore.community.repository.comment;

import com.somemore.community.domain.CommunityComment;

import java.util.Optional;

public interface CommunityCommentRepository {
    CommunityComment save(CommunityComment communityComment);
    Optional<CommunityComment> findById(Long id);
    boolean existsById(Long id);
    default boolean doesNotExistById(Long id) {
        return !existsById(id);
    }
    void deleteAllInBatch();
}
