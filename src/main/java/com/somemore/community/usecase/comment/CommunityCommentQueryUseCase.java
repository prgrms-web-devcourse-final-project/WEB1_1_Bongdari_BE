package com.somemore.community.usecase.comment;

import com.somemore.community.dto.response.CommunityCommentResponseDto;

import java.util.List;

public interface CommunityCommentQueryUseCase {
    List<CommunityCommentResponseDto> getCommunityCommentsByBoardId(Long boardId);
}
