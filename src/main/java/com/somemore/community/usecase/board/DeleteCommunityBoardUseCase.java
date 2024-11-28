package com.somemore.community.usecase.board;

import java.util.UUID;

public interface DeleteCommunityBoardUseCase {
    void deleteCommunityBoard(UUID writerId, Long id);
}