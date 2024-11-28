package com.somemore.community.repository.comment;

import com.somemore.community.domain.CommunityComment;

import java.util.Optional;

public interface CommunityCommentRepository {
    CommunityComment save(CommunityComment communityComment);
    Optional<CommunityComment> findById(Long id);
    void deleteAllInBatch();
}
