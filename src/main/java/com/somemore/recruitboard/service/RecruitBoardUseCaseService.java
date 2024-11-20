package com.somemore.recruitboard.service;

import com.somemore.recruitboard.dto.request.RecruitCreateRequestDto;
import java.util.Optional;

public interface RecruitBoardUseCaseService {

    public Long createRecruitBoard(
        RecruitCreateRequestDto requestDto,
        Long centerId,
        Optional<String> imgUrl);

}
