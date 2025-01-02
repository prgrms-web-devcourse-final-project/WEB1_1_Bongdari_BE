package com.somemore.domains.recruitboard.usecase.command;

import java.util.UUID;

public interface DeleteRecruitBoardUseCase {

    void deleteRecruitBoard(UUID centerId, Long id);
}
