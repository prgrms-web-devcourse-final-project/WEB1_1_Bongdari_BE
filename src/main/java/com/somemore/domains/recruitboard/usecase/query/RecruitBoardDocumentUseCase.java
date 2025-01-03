//package com.somemore.recruitboard.usecase.query;
//
//import com.somemore.recruitboard.domain.RecruitBoard;
//import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
//import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
//import com.somemore.recruitboard.dto.response.RecruitBoardDetailResponseDto;
//import com.somemore.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
//import org.springframework.data.domain.Page;
//
//import java.util.List;
//
//public interface RecruitBoardDocumentUseCase {
//    Page<RecruitBoardWithCenterResponseDto> getRecruitBoardBySearch(RecruitBoardSearchCondition condition);
//    Page<RecruitBoardDetailResponseDto> getRecruitBoardsNearbyWithKeyword(
//            RecruitBoardNearByCondition condition);
//    void saveRecruitBoardDocuments(List<RecruitBoard> recruitBoards);
//}
