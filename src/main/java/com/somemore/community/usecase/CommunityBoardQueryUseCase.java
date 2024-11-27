package com.somemore.community.usecase;

import com.somemore.community.dto.response.CommunityBoardGetDetailResponseDto;
import com.somemore.community.dto.response.CommunityBoardGetResponseDto;

import java.util.List;
import java.util.UUID;

public interface CommunityBoardQueryUseCase {
    List<CommunityBoardGetResponseDto> getCommunityBoards();
    List<CommunityBoardGetResponseDto> getCommunityBoardsByWriterId(UUID writerId);
    CommunityBoardGetDetailResponseDto getCommunityBoardDetail(Long id);
}
