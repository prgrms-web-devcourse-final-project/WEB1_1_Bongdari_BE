package com.somemore.recruitboard.repository;

import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.recruitboard.repository.mapper.RecruitBoardDetail;
import com.somemore.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.recruitboard.repository.mapper.RecruitBoardWithLocation;
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

    Page<RecruitBoard> findAllByCenterId(UUID centerId, RecruitBoardSearchCondition condition);

    List<Long> findNotCompletedIdsByCenterId(UUID centerId);

    List<RecruitBoard> findAllByIds(List<Long> ids);

    Page<RecruitBoardWithCenter> findByRecruitBoardsContaining(String keyword, RecruitBoardSearchCondition condition);
    void saveDocuments(List<RecruitBoard> recruitBoards);
    List<RecruitBoard> findAll();
    void deleteDocument(Long id);
}
