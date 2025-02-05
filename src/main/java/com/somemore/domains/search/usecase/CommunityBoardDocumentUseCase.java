package com.somemore.domains.search.usecase;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.dto.response.CommunityBoardResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommunityBoardDocumentUseCase {
    Page<CommunityBoardResponseDto> getCommunityBoardBySearch(String keyword, int page);
    void saveCommunityBoardDocuments(List<CommunityBoard> communityBoards);
}
