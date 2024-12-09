package com.somemore.community.service.comment;

import com.somemore.community.domain.CommunityComment;
import com.somemore.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.community.event.CommentAddedEvent;
import com.somemore.community.repository.board.CommunityBoardRepository;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import com.somemore.community.usecase.comment.CreateCommunityCommentUseCase;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.common.event.ServerEventType;
import com.somemore.global.exception.BadRequestException;
import com.somemore.notification.domain.NotificationSubType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_COMMENT;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateCommunityCommentService implements CreateCommunityCommentUseCase {

    private final CommunityBoardRepository communityBoardRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final ServerEventPublisher serverEventPublisher;

    @Override
    public Long createCommunityComment(CommunityCommentCreateRequestDto requestDto, UUID writerId, Long communityBoardId) {
        CommunityComment communityComment = requestDto.toEntity(writerId, communityBoardId);

        validateCommunityBoardExists(communityBoardId);

        if (requestDto.parentCommentId() != null) {
            validateParentCommentExists(communityComment.getParentCommentId());
        }

        publishCommentAddedEvent(communityComment);
        return communityCommentRepository.save(communityComment).getId();
    }

    private void validateCommunityBoardExists(Long communityBoardId) {
        if (communityBoardRepository.doesNotExistById(communityBoardId)) {
            throw new BadRequestException(NOT_EXISTS_COMMUNITY_BOARD.getMessage());
        }
    }

    private void validateParentCommentExists(Long parentCommentId) {
        if (communityCommentRepository.doesNotExistById(parentCommentId)) {
            throw new BadRequestException(NOT_EXISTS_COMMUNITY_COMMENT.getMessage());
        }
    }

    private void publishCommentAddedEvent(CommunityComment communityComment) {
        Long parentCommentId = communityComment.getParentCommentId();

        UUID targetVolunteerId = getTargetVolunteerId(communityComment, parentCommentId);

        CommentAddedEvent event = CommentAddedEvent.builder()
                .type(ServerEventType.NOTIFICATION)
                .subType(NotificationSubType.COMMENT_ADDED)
                .volunteerId(targetVolunteerId)
                .communityBoardId(communityComment.getCommunityBoardId())
                .build();

        serverEventPublisher.publish(event);
    }

    private UUID getTargetVolunteerId(CommunityComment communityComment, Long parentCommentId) {
        UUID targetVolunteerId;

        if (parentCommentId == null) {
            targetVolunteerId = communityBoardRepository.findById(communityComment.getCommunityBoardId())
                    .orElseThrow(EntityNotFoundException::new)
                    .getWriterId();

            return targetVolunteerId;
        }

        targetVolunteerId = communityCommentRepository.findById(parentCommentId)
                .map(CommunityComment::getWriterId)
                .orElse(null);

        return targetVolunteerId;
    }
}
