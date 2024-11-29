package com.somemore.community.repository.board;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.repository.mapper.CommunityBoardView;
import com.somemore.community.domain.QCommunityBoard;
import com.somemore.volunteer.domain.QVolunteer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CommunityBoardRepositoryImpl implements CommunityBoardRepository {

    private final JPAQueryFactory queryFactory;
    private final CommunityBoardJpaRepository communityBoardJpaRepository;

    @Override
    public CommunityBoard save(CommunityBoard communityBoard) {
        return communityBoardJpaRepository.save(communityBoard);
    }

    @Override
    public Optional<CommunityBoard> findById(Long id) {
        QCommunityBoard communityBoard = QCommunityBoard.communityBoard;

        return Optional.ofNullable(queryFactory
                .selectFrom(communityBoard)
                .where(communityBoard.id.eq(id)
                        .and(communityBoard.deleted.eq(false)))
                .fetchOne());
    }

    @Override
    public List<CommunityBoardView> getCommunityBoards() {
        return getCommunityBoardsQuery()
                .where(QCommunityBoard.communityBoard.deleted.eq(false))
                .fetch();
    }

    @Override
    public List<CommunityBoardView> findByWriterId(UUID writerId) {
        return getCommunityBoardsQuery()
                .where(QCommunityBoard.communityBoard.writerId.eq(writerId)
                        .and(QCommunityBoard.communityBoard.deleted.eq(false)))
                .fetch();
    }

    @Override
    public boolean existsById(Long id) {
        return communityBoardJpaRepository.existsByIdAndDeletedFalse(id);
    }

    private JPAQuery<CommunityBoardView> getCommunityBoardsQuery() {
        QCommunityBoard communityBoard = QCommunityBoard.communityBoard;
        QVolunteer volunteer = QVolunteer.volunteer;

        return queryFactory
                .select(Projections.constructor(CommunityBoardView.class,
                        communityBoard,
                        volunteer.nickname))
                .from(communityBoard)
                .join(volunteer).on(communityBoard.writerId.eq(volunteer.id))
                .orderBy(communityBoard.createdAt.desc());
    }


    @Override
    public void deleteAllInBatch() {
        communityBoardJpaRepository.deleteAllInBatch();
    }
}
