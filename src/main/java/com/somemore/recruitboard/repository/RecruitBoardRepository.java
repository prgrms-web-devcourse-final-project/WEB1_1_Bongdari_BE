package com.somemore.recruitboard.repository;

import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.mapping.RecruitBoardDetail;
import com.somemore.recruitboard.domain.mapping.RecruitBoardWithCenter;
import com.somemore.recruitboard.domain.mapping.RecruitBoardWithLocation;
import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
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
}
