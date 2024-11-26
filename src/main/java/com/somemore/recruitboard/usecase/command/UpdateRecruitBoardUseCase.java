package com.somemore.recruitboard.usecase.command;

import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import java.time.LocalDateTime;
import java.util.UUID;

public interface UpdateRecruitBoardUseCase {

    void updateRecruitBoard(
        RecruitBoardUpdateRequestDto requestDto,
        Long recruitBoardId,
        UUID centerId,
        String imgUrl
    );

    void updateRecruitBoardLocation(
        RecruitBoardLocationUpdateRequestDto requestDto,
        Long recruitBoardId,
        UUID centerId
    );

    void updateRecruitBoardStatus(
        RecruitStatus status,
        Long recruitBoardId,
        UUID centerId,
        LocalDateTime currentDateTime
    );

}
