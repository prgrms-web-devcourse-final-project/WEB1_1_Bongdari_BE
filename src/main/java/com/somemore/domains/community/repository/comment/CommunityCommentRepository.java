package com.somemore.domains.community.repository.comment;

import com.somemore.domains.community.domain.CommunityComment;
import com.somemore.domains.community.repository.mapper.CommunityCommentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommunityCommentRepository {
    CommunityComment save(CommunityComment communityComment);

    Optional<CommunityComment> findById(Long id);

    Page<CommunityCommentView> findCommentsByBoardId(Long boardId, Pageable pageable);

    boolean existsById(Long id);

    default boolean doesNotExistById(Long id) {
        return !existsById(id);
    }

    void deleteAllInBatch();
}
