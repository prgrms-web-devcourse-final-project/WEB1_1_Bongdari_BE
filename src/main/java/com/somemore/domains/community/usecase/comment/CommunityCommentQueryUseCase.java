package com.somemore.domains.community.usecase.comment;

import com.somemore.domains.community.dto.response.CommunityCommentResponseDto;
import org.springframework.data.domain.Page;

public interface CommunityCommentQueryUseCase {
    Page<CommunityCommentResponseDto> getCommunityCommentsByBoardId(Long boardId, int page);
}
