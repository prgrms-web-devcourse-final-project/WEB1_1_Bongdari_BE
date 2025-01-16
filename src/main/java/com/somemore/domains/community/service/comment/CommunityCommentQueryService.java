package com.somemore.domains.community.service.comment;

import com.somemore.domains.community.domain.CommunityComment;
import com.somemore.domains.community.dto.response.CommunityCommentResponseDto;
import com.somemore.domains.community.repository.comment.CommunityCommentRepository;
import com.somemore.domains.community.repository.mapper.CommunityCommentView;
import com.somemore.domains.community.usecase.comment.CommunityCommentQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommunityCommentQueryService implements CommunityCommentQueryUseCase {

    private final CommunityCommentRepository communityCommentRepository;
    private static final int PAGE_SIZE = 4;

    @Override
    public Page<CommunityCommentResponseDto> getCommunityCommentsByBoardId(Long boardId, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<CommunityCommentView> commentPage = communityCommentRepository.findCommentsByBoardId(boardId, pageable);
        List<CommunityCommentView> filteredComments = filterValidComments(commentPage.getContent());
        List<CommunityCommentResponseDto> hierarchicalComments = createCommentHierarchy(filteredComments);

        return new PageImpl<>(hierarchicalComments, pageable, filteredComments.size());
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

        if (comment.isDeleted()) {
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
            CommunityCommentResponseDto dto = CommunityCommentResponseDto.from(comment);
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

