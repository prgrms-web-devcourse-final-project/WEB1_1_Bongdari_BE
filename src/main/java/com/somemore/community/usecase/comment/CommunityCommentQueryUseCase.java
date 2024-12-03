package com.somemore.community.usecase.comment;

import com.somemore.community.dto.response.CommunityCommentResponseDto;
import org.springframework.data.domain.Page;

public interface CommunityCommentQueryUseCase {
    Page<CommunityCommentResponseDto> getCommunityCommentsByBoardId(Long boardId, int page);
}
