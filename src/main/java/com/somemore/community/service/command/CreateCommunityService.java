package com.somemore.community.service.command;

import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.request.CommunityCreateRequestDto;
import com.somemore.community.repository.CommunityRepository;
import com.somemore.community.usecase.CreateCommunityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateCommunityService implements CreateCommunityUseCase {

    private final CommunityRepository communityRepository;

    @Override
    public Long createCommunityBoard(CommunityCreateRequestDto requestDto, UUID writerId, String imgUrl) {

        CommunityBoard communityBoard = requestDto.toEntity(writerId, imgUrl != null ? imgUrl : "");

        communityRepository.save(communityBoard);

        return communityBoard.getId();
    }
}
