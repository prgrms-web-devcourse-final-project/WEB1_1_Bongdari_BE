package com.somemore.recruitboard.usecase.command;

import com.somemore.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import java.util.UUID;

public interface UpdateRecruitBoardUseCase {

    public void updateRecruitBoard(
        RecruitBoardUpdateRequestDto requestDto,
        Long recruitBoardId,
        UUID centerId,
        String imgUrl
    );

    public void updateRecruitBoardLocation(
        RecruitBoardLocationUpdateRequestDto requestDto,
        Long recruitBoardId,
        UUID centerId
    );

}
