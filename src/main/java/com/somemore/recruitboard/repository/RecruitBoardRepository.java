package com.somemore.recruitboard.repository;

import com.somemore.recruitboard.domain.RecruitBoard;
import java.util.Optional;

public interface RecruitBoardRepository {


    RecruitBoard save(RecruitBoard recruitBoard);

    RecruitBoard saveAndFlush(RecruitBoard recruitBoard);

    Optional<RecruitBoard> findById(Long id);

    void deleteAllInBatch();
}
