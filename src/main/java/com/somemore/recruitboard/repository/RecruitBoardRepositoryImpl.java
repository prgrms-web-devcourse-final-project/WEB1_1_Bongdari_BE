package com.somemore.recruitboard.repository;

import com.somemore.recruitboard.domain.RecruitBoard;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RecruitBoardRepositoryImpl implements RecruitBoardRepository {

    private final RecruitBoardJpaRepository recruitBoardJpaRepository;

    @Override
    public RecruitBoard save(RecruitBoard recruitBoard) {
        return recruitBoardJpaRepository.save(recruitBoard);
    }

    @Override
    public RecruitBoard saveAndFlush(RecruitBoard recruitBoard) {
        return recruitBoardJpaRepository.saveAndFlush(recruitBoard);
    }

    @Override
    public Optional<RecruitBoard> findById(Long id) {
        return recruitBoardJpaRepository.findById(id);
    }

    @Override
    public void deleteAllInBatch() {
        recruitBoardJpaRepository.deleteAllInBatch();
    }


}
