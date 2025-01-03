//package com.somemore.recruitboard.service.query;
//
//import com.somemore.recruitboard.domain.RecruitBoard;
//import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
//import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
//import com.somemore.recruitboard.dto.response.RecruitBoardDetailResponseDto;
//import com.somemore.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
//import com.somemore.recruitboard.repository.RecruitBoardRepository;
//import com.somemore.recruitboard.repository.mapper.RecruitBoardDetail;
//import com.somemore.recruitboard.repository.mapper.RecruitBoardWithCenter;
//import com.somemore.recruitboard.usecase.query.RecruitBoardDocumentUseCase;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class RecruitBoardDocumentService implements RecruitBoardDocumentUseCase {
//
//    private final RecruitBoardRepository recruitBoardRepository;
//
//    @Transactional(readOnly = true)
//    @Override
//    public Page<RecruitBoardWithCenterResponseDto> getRecruitBoardBySearch(RecruitBoardSearchCondition condition) {
//        Page<RecruitBoardWithCenter> boards = recruitBoardRepository.findByRecruitBoardsContaining(condition);
//        return boards.map(RecruitBoardWithCenterResponseDto::from);
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public Page<RecruitBoardDetailResponseDto> getRecruitBoardsNearbyWithKeyword(
//            RecruitBoardNearByCondition condition) {
//        Page<RecruitBoardDetail> boards = recruitBoardRepository.findAllNearbyWithKeyword(condition);
//        return boards.map(RecruitBoardDetailResponseDto::from);
//    }
//
//    @Transactional
//    @Override
//    public void saveRecruitBoardDocuments(List<RecruitBoard> recruitBoards) {
//        recruitBoardRepository.saveDocuments(recruitBoards);
//    }
//}
