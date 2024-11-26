package com.somemore.recruitboard.usecase.command;

import java.util.UUID;

public interface DeleteRecruitBoardUseCase {

    void deleteRecruitBoard(UUID centerId, Long recruitBoardId);
}
