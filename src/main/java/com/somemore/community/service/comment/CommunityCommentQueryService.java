package com.somemore.community.service.comment;

import com.somemore.community.domain.CommunityComment;
import com.somemore.community.dto.response.CommunityCommentResponseDto;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import com.somemore.community.repository.mapper.CommunityCommentView;
import com.somemore.community.usecase.comment.CommunityCommentQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommunityCommentQueryService implements CommunityCommentQueryUseCase {

    private final CommunityCommentRepository communityCommentRepository;

    @Override
    public List<CommunityCommentResponseDto> getCommunityCommentsByBoardId(Long boardId) {
        List<CommunityCommentView> allComments = communityCommentRepository.findCommentsByBoardId(boardId);
        List<CommunityCommentView> filteredComments = filterValidComments(allComments);
        return createCommentHierarchy(filteredComments);
    }

    private List<CommunityCommentView> filterValidComments(List<CommunityCommentView> comments) {
        List<Long> parentCommentIds = findParentCommentIds(comments);

        return comments.stream()
                .flatMap(comment -> processDeletedComment(parentCommentIds, comment).stream())
                .toList();
    }

    private List<Long> findParentCommentIds(List<CommunityCommentView> comments) {
        return comments.stream()
                .filter(comment -> !comment.communityComment().getDeleted())
                .map(comment -> comment.communityComment().getParentCommentId())
                .filter(Objects::nonNull)
                .toList();
    }

    private Optional<CommunityCommentView> processDeletedComment(List<Long> parentCommentIds, CommunityCommentView commentView) {
        CommunityComment comment = commentView.communityComment();

        if (comment.getDeleted()) {
            if (parentCommentIds.contains(comment.getId())) {
                return Optional.of(commentView.replaceWriterNickname(commentView));
            }
            return Optional.empty();
        }

        return Optional.of(commentView);
    }

    private List<CommunityCommentResponseDto> createCommentHierarchy(List<CommunityCommentView> comments) {

        Map<Long, CommunityCommentResponseDto> commentMap = new HashMap<>();
        List<CommunityCommentResponseDto> rootComments = new ArrayList<>();

        for (CommunityCommentView comment : comments) {
            CommunityCommentResponseDto dto = CommunityCommentResponseDto.fromView(comment);
            commentMap.put(dto.id(), dto);

            Long parentCommentId = comment.communityComment().getParentCommentId();

            if (parentCommentId == null) {
                rootComments.add(dto);
            } else {
                commentMap.get(parentCommentId).addReply(dto);
            }
        }

        return rootComments;
    }
}

