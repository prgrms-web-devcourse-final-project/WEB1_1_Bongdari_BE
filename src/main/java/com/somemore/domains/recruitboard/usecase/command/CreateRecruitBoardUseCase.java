package com.somemore.domains.recruitboard.usecase.command;

import com.somemore.domains.recruitboard.dto.request.RecruitBoardCreateRequestDto;
import java.util.UUID;

public interface CreateRecruitBoardUseCase {

    Long createRecruitBoard(RecruitBoardCreateRequestDto requestDto, UUID centerId, String imgUrl);
}
