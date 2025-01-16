package com.somemore.domains.community.service.comment;

import com.somemore.domains.community.domain.CommunityComment;
import com.somemore.domains.community.dto.request.CommunityCommentUpdateRequestDto;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.repository.comment.CommunityCommentRepository;
import com.somemore.domains.community.usecase.comment.UpdateCommunityCommentUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_COMMENT;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_COMMUNITY_COMMENT;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateCommunityCommentService implements UpdateCommunityCommentUseCase {

    private final CommunityCommentRepository communityCommentRepository;
    private final CommunityBoardRepository communityBoardRepository;

    @Override
    public void updateCommunityComment(CommunityCommentUpdateRequestDto requestDto, Long communityCommentId, UUID writerId, Long communityBoardId) {

        CommunityComment communityComment = getCommunityCommentById(communityCommentId);

        validateCommunityBoardExists(communityBoardId);

        validateWriter(communityComment, writerId);

        communityComment.updateWith(requestDto);

        communityCommentRepository.save(communityComment);
    }

    private CommunityComment getCommunityCommentById(Long id) {
        return communityCommentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_COMMUNITY_COMMENT.getMessage()));
    }

    private void validateCommunityBoardExists(Long communityBoardId) {
        if (communityBoardRepository.doesNotExistById(communityBoardId)) {
            throw new BadRequestException(NOT_EXISTS_COMMUNITY_BOARD.getMessage());
        }
    }

    private void validateWriter(CommunityComment communityComment, UUID writerId) {
        if (communityComment.isWriter(writerId)) {
            return;
        }
        
        throw new BadRequestException(UNAUTHORIZED_COMMUNITY_COMMENT.getMessage());
    }
}
