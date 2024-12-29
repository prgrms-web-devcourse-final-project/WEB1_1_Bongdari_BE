package com.somemore.domains.search.service;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardDetail;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.domains.search.repository.SearchBoardRepository;
import com.somemore.domains.search.usecase.RecruitBoardDocumentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(name = "elastic.search.enabled", havingValue = "true", matchIfMissing = true)
public class RecruitBoardDocumentService implements RecruitBoardDocumentUseCase {

    private final SearchBoardRepository searchBoardRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<RecruitBoardWithCenterResponseDto> getRecruitBoardBySearch(RecruitBoardSearchCondition condition) {
        Page<RecruitBoardWithCenter> boards = searchBoardRepository.findByRecruitBoardsContaining(condition);
        return boards.map(RecruitBoardWithCenterResponseDto::from);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RecruitBoardDetailResponseDto> getRecruitBoardsNearbyWithKeyword(
            RecruitBoardNearByCondition condition) {
        Page<RecruitBoardDetail> boards = searchBoardRepository.findAllNearbyWithKeyword(condition);
        return boards.map(RecruitBoardDetailResponseDto::from);
    }

    @Transactional
    @Override
    public void saveRecruitBoardDocuments(List<RecruitBoard> recruitBoards) {
        searchBoardRepository.saveRecruitBoardDocuments(recruitBoards);
    }
}
