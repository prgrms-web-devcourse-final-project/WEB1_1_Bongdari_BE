package com.somemore.community.service.comment;

import com.somemore.community.domain.CommunityComment;
import com.somemore.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import com.somemore.community.usecase.comment.CreateCommunityCommentUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_COMMENT;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateCommunityCommentService implements CreateCommunityCommentUseCase {

    private final CommunityCommentRepository communityCommentRepository;

    @Override
    public Long createCommunityComment(CommunityCommentCreateRequestDto requestDto, UUID writerId) {
        CommunityComment communityComment = requestDto.toEntity(writerId);

        validateParentCommentExists(communityComment.getParentCommentId());

        return communityCommentRepository.save(communityComment).getId();
    }

    private void validateParentCommentExists(Long parentCommentId) {
        if (parentCommentId != null) {
            communityCommentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new BadRequestException(NOT_EXISTS_COMMUNITY_COMMENT.getMessage()));
        }
    }

}
