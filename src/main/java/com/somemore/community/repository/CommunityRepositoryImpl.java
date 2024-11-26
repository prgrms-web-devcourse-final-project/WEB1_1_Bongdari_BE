package com.somemore.community.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.domain.QCommunityBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CommunityRepositoryImpl implements CommunityBoardRepository {

    private final JPAQueryFactory queryFactory;
    private final CommunityBoardJpaRepository communityBoardJpaRepository;

    @Override
    public CommunityBoard save(CommunityBoard communityBoard) {
        return communityBoardJpaRepository.save(communityBoard);
    }

    @Override
    public Optional<CommunityBoard> getCommunityBoardWithId(Long id) {
        QCommunityBoard communityBoard = QCommunityBoard.communityBoard;

        return Optional.ofNullable(queryFactory
                .selectFrom(communityBoard)
                .where(communityBoard.id.eq(id)
                        .and(communityBoard.deleted.eq(false)))
                .fetchOne());
    }

    @Override
    public List<CommunityBoard> getCommunityBoards() {
        QCommunityBoard communityBoard = QCommunityBoard.communityBoard;

        return queryFactory
                .selectFrom(communityBoard)
                .where(communityBoard.deleted.eq(false))
                .orderBy(communityBoard.createdAt.desc())
                .fetch();
    }

    @Override
    public List<CommunityBoard> getCommunityBoardsByWriterId(UUID writerId) {
        QCommunityBoard communityBoard = QCommunityBoard.communityBoard;

        return queryFactory
                .selectFrom(communityBoard)
                .where(communityBoard.writerId.eq(writerId)
                        .and(communityBoard.deleted.eq(false))
                )
                .orderBy(communityBoard.createdAt.desc())
                .fetch();
    }

    @Override
    public void deleteAllInBatch() {
        communityBoardJpaRepository.deleteAllInBatch();
    }
}
