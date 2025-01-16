package com.somemore.domains.community.service.board;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.dto.request.CommunityBoardUpdateRequestDto;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.usecase.board.UpdateCommunityBoardUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_COMMUNITY_BOARD;


@RequiredArgsConstructor
@Transactional
@Service
public class UpdateCommunityBoardService implements UpdateCommunityBoardUseCase {

    private final CommunityBoardRepository communityBoardRepository;

    @Override
    public void updateCommunityBoard(CommunityBoardUpdateRequestDto requestDto, Long communityBoardId, UUID writerId, String imgUrl) {
        CommunityBoard communityBoard = getCommunityBoardById(communityBoardId);
        validateWriter(communityBoard, writerId);
        communityBoard.updateWith(requestDto, imgUrl);

        communityBoardRepository.save(communityBoard);
    }

    private CommunityBoard getCommunityBoardById(Long id) {
        return communityBoardRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_COMMUNITY_BOARD.getMessage()));
    }

    private void validateWriter(CommunityBoard communityBoard, UUID writerId) {
        if (communityBoard.isWriter(writerId)) {
            return;
        }

        throw new BadRequestException(UNAUTHORIZED_COMMUNITY_BOARD.getMessage());
    }
}
