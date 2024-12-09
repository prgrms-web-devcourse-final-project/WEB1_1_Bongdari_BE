package com.somemore.community.usecase.board;

import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.response.CommunityBoardResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommunityBoardDocumentUseCase {
    Page<CommunityBoardResponseDto> getCommunityBoardBySearch(String keyword, int page);
    void saveCommunityBoardDocuments(List<CommunityBoard> communityBoards);
}
