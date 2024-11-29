package com.somemore.recruitboard.usecase.query;

import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithLocationResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface RecruitBoardQueryUseCase {

    RecruitBoardResponseDto getById(Long id);

    RecruitBoardWithLocationResponseDto getWithLocationById(Long id);

    Page<RecruitBoardWithCenterResponseDto> getAllWithCenter(RecruitBoardSearchCondition condition);

    Page<RecruitBoardDetailResponseDto> getRecruitBoardsNearby(
        RecruitBoardNearByCondition condition);

    Page<RecruitBoardResponseDto> getRecruitBoardsByCenterId(UUID centerId,
        RecruitBoardSearchCondition condition);
}
