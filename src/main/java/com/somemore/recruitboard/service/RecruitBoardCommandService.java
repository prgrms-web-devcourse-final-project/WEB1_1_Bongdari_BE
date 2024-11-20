package com.somemore.recruitboard.service;

import com.somemore.recruitboard.dto.command.RecruitBoardCreateCommandRequestDto;

public interface RecruitBoardCommandService {

    public Long create(RecruitBoardCreateCommandRequestDto dto);
}
