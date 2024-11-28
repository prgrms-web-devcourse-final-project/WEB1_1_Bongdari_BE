package com.somemore.community.service.comment;

import com.somemore.community.domain.CommunityComment;
import com.somemore.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import com.somemore.community.usecase.comment.CreateCommunityCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateCommunityCommentService implements CreateCommunityCommentUseCase {

    private final CommunityCommentRepository communityCommentRepository;

    @Override
    public Long CreateCommunityComment(CommunityCommentCreateRequestDto requestDto, UUID writerId, Long parentCommunityId) {

        CommunityComment communityComment = requestDto.toEntity(writerId, parentCommunityId);

        communityCommentRepository.save(communityComment);

        return communityComment.getId();
    }
}
