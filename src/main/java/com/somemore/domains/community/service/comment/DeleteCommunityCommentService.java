package com.somemore.domains.community.service.comment;

import com.somemore.domains.community.domain.CommunityComment;
import com.somemore.domains.community.repository.comment.CommunityCommentRepository;
import com.somemore.domains.community.usecase.comment.DeleteCommunityCommentUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_COMMENT;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_COMMUNITY_COMMENT;

@RequiredArgsConstructor
@Transactional
@Service
public class DeleteCommunityCommentService implements DeleteCommunityCommentUseCase {

    private final CommunityCommentRepository communityCommentRepository;

    @Override
    public void deleteCommunityComment(UUID writerId, Long id, Long communityBoardId) {

        CommunityComment communityComment = getCommunityCommentById(id);

        validateWriter(communityComment, writerId);

        communityComment.markAsDeleted();

        communityCommentRepository.save(communityComment);
    }

    private CommunityComment getCommunityCommentById(Long id) {
        return communityCommentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_COMMUNITY_COMMENT.getMessage()));
    }

    private void validateWriter(CommunityComment communityComment, UUID writerId) {
        if (communityComment.isWriter(writerId)) {
            return;
        }

        throw new BadRequestException(UNAUTHORIZED_COMMUNITY_COMMENT.getMessage());
    }
}
