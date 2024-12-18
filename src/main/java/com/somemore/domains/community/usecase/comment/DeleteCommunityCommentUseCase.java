package com.somemore.domains.community.usecase.comment;

import java.util.UUID;

public interface DeleteCommunityCommentUseCase {
    void deleteCommunityComment(UUID writerId, Long id, Long communityBoardId);
}
