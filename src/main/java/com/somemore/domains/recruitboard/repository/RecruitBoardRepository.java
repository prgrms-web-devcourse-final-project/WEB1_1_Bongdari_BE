package com.somemore.domains.recruitboard.repository;

import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardDetail;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithLocation;
import com.somemore.domains.recruitboard.domain.RecruitBoard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface RecruitBoardRepository {

    RecruitBoard save(RecruitBoard recruitBoard);

    List<RecruitBoard> saveAll(List<RecruitBoard> recruitBoards);

    Optional<RecruitBoard> findById(Long id);

    Optional<RecruitBoardWithLocation> findWithLocationById(Long id);

    Page<RecruitBoardWithCenter> findAllWithCenter(RecruitBoardSearchCondition condition);

    Page<RecruitBoardDetail> findAllNearby(RecruitBoardNearByCondition condition);

//    Page<RecruitBoardDetail> findAllNearbyWithKeyword(RecruitBoardNearByCondition condition);


    Page<RecruitBoard> findAllByCenterId(UUID centerId, RecruitBoardSearchCondition condition);

    List<Long> findNotCompletedIdsByCenterId(UUID centerId);

    List<RecruitBoard> findAllByIds(List<Long> ids);

//    Page<RecruitBoardWithCenter> findByRecruitBoardsContaining(RecruitBoardSearchCondition condition);
//    void saveDocuments(List<RecruitBoard> recruitBoards);
    List<RecruitBoard> findAll();
//    void deleteDocument(Long id);
}
