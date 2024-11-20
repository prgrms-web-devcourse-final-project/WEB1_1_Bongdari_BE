package com.somemore.recruitboard.usecase;

import com.somemore.recruitboard.dto.request.RecruitBoardCreateRequestDto;
import java.util.UUID;

public interface CreateRecruitBoardUseCase {

    Long createRecruitBoard(RecruitBoardCreateRequestDto requestDto, UUID centerId, String imgUrl);
}
