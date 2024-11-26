package com.somemore.community.service.command;

import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.repository.CommunityBoardRepository;
import com.somemore.community.usecase.command.DeleteCommunityBoardUseCase;
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
class DeleteCommunityBoardService implements DeleteCommunityBoardUseCase {

    private final CommunityBoardRepository communityBoardRepository;

    @Override
    public void deleteCommunityBoard(UUID writerId, Long id) {
        CommunityBoard communityBoard = communityBoardRepository.getCommunityBoardWithId(id)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_COMMUNITY_BOARD.getMessage()));

        validateWriter(communityBoard, writerId);

        communityBoard.markAsDeleted();
    }

    private void validateWriter(CommunityBoard communityBoard, UUID writerId) {
        if (communityBoard.getWriterId().equals(writerId)) {
            return;
        }

        throw new BadRequestException(UNAUTHORIZED_COMMUNITY_BOARD.getMessage());
    }
}