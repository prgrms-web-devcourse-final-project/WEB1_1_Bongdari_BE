package com.somemore.domains.search.usecase;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RecruitBoardDocumentUseCase {
    Page<RecruitBoardWithCenterResponseDto> getRecruitBoardBySearch(RecruitBoardSearchCondition condition);
    Page<RecruitBoardDetailResponseDto> getRecruitBoardsNearbyWithKeyword(
            RecruitBoardNearByCondition condition);
    void saveRecruitBoardDocuments(List<RecruitBoard> recruitBoards);
}
