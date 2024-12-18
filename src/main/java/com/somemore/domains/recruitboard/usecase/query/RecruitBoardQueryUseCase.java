package com.somemore.domains.recruitboard.usecase.query;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithLocationResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface RecruitBoardQueryUseCase {

    RecruitBoard getById(Long id);

    RecruitBoardResponseDto getRecruitBoardById(Long id);

    RecruitBoardWithLocationResponseDto getWithLocationById(Long id);

    Page<RecruitBoardWithCenterResponseDto> getAllWithCenter(RecruitBoardSearchCondition condition);

    Page<RecruitBoardDetailResponseDto> getRecruitBoardsNearby(
            RecruitBoardNearByCondition condition);

    Page<RecruitBoardResponseDto> getRecruitBoardsByCenterId(UUID centerId,
            RecruitBoardSearchCondition condition);

    List<Long> getNotCompletedIdsByCenterIds(UUID centerId);

    List<RecruitBoard> getAllByIds(List<Long> ids);

    List<RecruitBoard> getAllRecruitBoards();

}
