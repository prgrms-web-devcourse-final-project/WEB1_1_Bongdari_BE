package com.somemore.community.service.board;

import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.response.CommunityBoardResponseDto;
import com.somemore.community.repository.board.CommunityBoardRepository;
import com.somemore.community.repository.mapper.CommunityBoardView;
import com.somemore.community.usecase.board.CommunityBoardDocumentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityBoardDocumentService implements CommunityBoardDocumentUseCase {

    private final CommunityBoardRepository communityBoardRepository;
    private static final int PAGE_SIZE = 10;

    @Transactional(readOnly = true)
    @Override
    public Page<CommunityBoardResponseDto> getCommunityBoardBySearch(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<CommunityBoardView> boards = communityBoardRepository.findByCommunityBoardsContaining(keyword, pageable);
        return boards.map(CommunityBoardResponseDto::from);
    }

    @Transactional
    @Override
    public void saveCommunityBoardDocuments(List<CommunityBoard> communityBoards) {
        communityBoardRepository.saveDocuments(communityBoards);
    }
}
