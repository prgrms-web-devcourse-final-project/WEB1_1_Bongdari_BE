package com.somemore.domains.community.service.board;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.usecase.board.DeleteCommunityBoardUseCase;
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
        CommunityBoard communityBoard = getCommunityBoardById(id);

        validateWriter(communityBoard, writerId);

        communityBoard.markAsDeleted();

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