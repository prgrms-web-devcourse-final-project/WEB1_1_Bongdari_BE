package com.somemore.community.service;

import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.domain.CommunityBoardView;
import com.somemore.community.dto.response.CommunityBoardGetDetailResponseDto;
import com.somemore.community.dto.response.CommunityBoardGetResponseDto;
import com.somemore.community.repository.CommunityBoardRepository;
import com.somemore.community.usecase.CommunityBoardQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommunityBoardQueryService implements CommunityBoardQueryUseCase {

    private final CommunityBoardRepository communityBoardRepository;

    @Override
    public List<CommunityBoardGetResponseDto> getCommunityBoards() {
        List<CommunityBoardView> boards = communityBoardRepository.getCommunityBoards();
        return mapEntitiesToDtos(boards, CommunityBoardGetResponseDto::fromEntity);
    }

    @Override
    public List<CommunityBoardGetResponseDto> getCommunityBoardsByWriterId(UUID writerId) {
        List<CommunityBoardView> boards = communityBoardRepository.findByWriterId(writerId);
        return mapEntitiesToDtos(boards, CommunityBoardGetResponseDto::fromEntity);
    }

    @Override
    public CommunityBoardGetDetailResponseDto getCommunityBoardDetail(Long id) {
        CommunityBoard board = communityBoardRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_COMMUNITY_BOARD.getMessage()));
        return CommunityBoardGetDetailResponseDto.fromEntity(board);
    }

    private <T, R> List<R> mapEntitiesToDtos(List<T> entities, Function<T, R> mapper) {
        return entities.stream()
                .map(mapper)
                .toList();
    }
}
