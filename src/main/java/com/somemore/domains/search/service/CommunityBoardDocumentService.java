package com.somemore.domains.search.service;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.dto.response.CommunityBoardResponseDto;
import com.somemore.domains.search.domain.CommunityBoardDocument;
import com.somemore.domains.search.repository.SearchBoardRepository;
import com.somemore.domains.search.usecase.CommunityBoardDocumentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(name = "elastic.search.enabled", havingValue = "true", matchIfMissing = true)
public class CommunityBoardDocumentService implements CommunityBoardDocumentUseCase {

    private final SearchBoardRepository searchBoardRepository;
    private static final int PAGE_SIZE = 10;

    @Transactional(readOnly = true)
    @Override
    public Page<CommunityBoardResponseDto> getCommunityBoardBySearch(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<CommunityBoardDocument> boards = searchBoardRepository.findByCommunityBoardsContaining(keyword, pageable);
        return boards.map(CommunityBoardResponseDto::fromDocument);
    }

    @Transactional
    @Override
    public void saveCommunityBoardDocuments(List<CommunityBoard> communityBoards) {
        searchBoardRepository.saveCommunityBoardDocuments(communityBoards);
    }
}
