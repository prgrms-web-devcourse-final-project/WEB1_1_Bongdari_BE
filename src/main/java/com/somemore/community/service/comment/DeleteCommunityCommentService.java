package com.somemore.community.service.comment;

import com.somemore.community.domain.CommunityComment;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import com.somemore.community.usecase.comment.DeleteCommunityCommentUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.*;

@RequiredArgsConstructor
@Transactional
@Service
public class DeleteCommunityCommentService implements DeleteCommunityCommentUseCase {

    private final CommunityCommentRepository communityCommentRepository;

    @Override
    public void deleteCommunityComment(UUID writerId, Long id) {

        CommunityComment communityComment = getCommunityBoardById(id);

        validateWriter(communityComment, writerId);

        communityComment.markAsDeleted();

        communityCommentRepository.save(communityComment);
    }

    private CommunityComment getCommunityBoardById(Long id) {
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
