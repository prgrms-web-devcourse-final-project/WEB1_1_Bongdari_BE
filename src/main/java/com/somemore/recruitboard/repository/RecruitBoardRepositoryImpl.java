package com.somemore.recruitboard.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.recruitboard.domain.QRecruitBoard;
import com.somemore.recruitboard.domain.RecruitBoard;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RecruitBoardRepositoryImpl implements RecruitBoardRepository {

    private final RecruitBoardJpaRepository recruitBoardJpaRepository;
    private final JPAQueryFactory queryFactory;

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
        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;

        RecruitBoard result = queryFactory
            .selectFrom(recruitBoard)
            .where(isNotDeleted().and(recruitBoard.id.eq(id)))
            .fetchOne();

        return Optional.ofNullable(result);
    }
    
    @Override
    public void deleteAllInBatch() {
        recruitBoardJpaRepository.deleteAllInBatch();
    }

    private BooleanExpression isNotDeleted() {
        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;
        return recruitBoard.deleted.eq(false);
    }
}
