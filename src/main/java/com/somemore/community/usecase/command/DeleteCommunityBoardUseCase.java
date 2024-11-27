package com.somemore.community.usecase.command;

import java.util.UUID;

public interface DeleteCommunityBoardUseCase {
    void deleteCommunityBoard(UUID writerId, Long id);
}