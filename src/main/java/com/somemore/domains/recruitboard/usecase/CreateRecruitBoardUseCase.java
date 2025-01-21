package com.somemore.domains.recruitboard.usecase;

import com.somemore.domains.recruitboard.dto.request.RecruitBoardCreateRequestDto;

import java.util.UUID;

public interface CreateRecruitBoardUseCase {

    Long createRecruitBoard(RecruitBoardCreateRequestDto requestDto, UUID centerId);
}
