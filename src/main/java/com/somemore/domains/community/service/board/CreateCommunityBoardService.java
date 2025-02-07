package com.somemore.domains.community.service.board;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.usecase.board.CreateCommunityBoardUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateCommunityBoardService implements CreateCommunityBoardUseCase {

    private final CommunityBoardRepository communityBoardRepository;

    @Override
    public Long createCommunityBoard(CommunityBoardCreateRequestDto requestDto, UUID writerId) {

        CommunityBoard communityBoard = requestDto.toEntity(writerId);

        communityBoardRepository.save(communityBoard);

        return communityBoard.getId();
    }
}
