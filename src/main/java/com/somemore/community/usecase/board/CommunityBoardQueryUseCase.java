package com.somemore.community.usecase.board;

import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.response.CommunityBoardDetailResponseDto;
import com.somemore.community.dto.response.CommunityBoardResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CommunityBoardQueryUseCase {
    Page<CommunityBoardResponseDto> getCommunityBoards(int page);
    Page<CommunityBoardResponseDto> getCommunityBoardsByWriterId(UUID writerId, int page);
    CommunityBoardDetailResponseDto getCommunityBoardDetail(Long id);
    List<CommunityBoard> getAllCommunityBoards();
}
