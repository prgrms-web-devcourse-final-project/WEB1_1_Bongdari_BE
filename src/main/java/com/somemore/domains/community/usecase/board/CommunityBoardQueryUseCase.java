package com.somemore.domains.community.usecase.board;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.dto.response.CommunityBoardDetailResponseDto;
import com.somemore.domains.community.dto.response.CommunityBoardResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CommunityBoardQueryUseCase {
    Page<CommunityBoardResponseDto> getCommunityBoards(String keyword, int page);

    Page<CommunityBoardResponseDto> getCommunityBoardsByWriterId(UUID writerId, int page);

    CommunityBoardDetailResponseDto getCommunityBoardDetail(Long id);

    List<CommunityBoard> getAllCommunityBoards();
}
