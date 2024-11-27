package com.somemore.community.service;

import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.community.repository.CommunityBoardRepository;
import com.somemore.community.usecase.CreateCommunityBoardUseCase;
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
    public Long createCommunityBoard(CommunityBoardCreateRequestDto requestDto, UUID writerId, String imgUrl) {

        CommunityBoard communityBoard = requestDto.toEntity(writerId, imgUrl == null ? "" : imgUrl);

        communityBoardRepository.save(communityBoard);

        return communityBoard.getId();
    }
}
