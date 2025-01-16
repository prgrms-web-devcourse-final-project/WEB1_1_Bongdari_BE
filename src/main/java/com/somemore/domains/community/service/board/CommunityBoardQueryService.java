package com.somemore.domains.community.service.board;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.dto.response.CommunityBoardDetailResponseDto;
import com.somemore.domains.community.dto.response.CommunityBoardResponseDto;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.repository.mapper.CommunityBoardView;
import com.somemore.domains.community.usecase.board.CommunityBoardQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommunityBoardQueryService implements CommunityBoardQueryUseCase {

    private final CommunityBoardRepository communityBoardRepository;
    private static final int PAGE_SIZE = 10;

    @Override
    public Page<CommunityBoardResponseDto> getCommunityBoards(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<CommunityBoardView> boards = communityBoardRepository.findCommunityBoards(keyword, pageable);
        return boards.map(CommunityBoardResponseDto::from);
    }

    @Override
    public Page<CommunityBoardResponseDto> getCommunityBoardsByWriterId(UUID writerId, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<CommunityBoardView> boards = communityBoardRepository.findByWriterId(writerId ,pageable);
        return boards.map(CommunityBoardResponseDto::from);
    }

    @Override
    public CommunityBoardDetailResponseDto getCommunityBoardDetail(Long id) {
        CommunityBoard board = communityBoardRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_COMMUNITY_BOARD.getMessage()));
        return CommunityBoardDetailResponseDto.from(board);
    }

    @Override
    public List<CommunityBoard> getAllCommunityBoards() {
        return communityBoardRepository.findAll();
    }
}
