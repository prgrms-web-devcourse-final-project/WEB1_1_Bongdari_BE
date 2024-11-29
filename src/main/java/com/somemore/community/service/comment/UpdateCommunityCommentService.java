package com.somemore.community.service.comment;

import com.somemore.community.domain.CommunityComment;
import com.somemore.community.dto.request.CommunityCommentUpdateRequestDto;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import com.somemore.community.usecase.comment.UpdateCommunityCommentUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.*;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateCommunityCommentService implements UpdateCommunityCommentUseCase {

    private final CommunityCommentRepository communityCommentRepository;

    @Override
    public void updateCommunityComment(CommunityCommentUpdateRequestDto requestDto, Long communityCommentId, UUID writerId) {

        CommunityComment communityComment = getCommunityCommentById(communityCommentId);
        validateWriter(communityComment, writerId);
        communityComment.updateWith(requestDto);

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
