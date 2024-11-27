package com.somemore.community.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.domain.CommunityBoardWithNickname;
import com.somemore.community.domain.QCommunityBoard;
import com.somemore.volunteer.domain.QVolunteer;
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
    public Optional<CommunityBoard> findById(Long id) {
        QCommunityBoard communityBoard = QCommunityBoard.communityBoard;

        return Optional.ofNullable(queryFactory
                .selectFrom(communityBoard)
                .where(communityBoard.id.eq(id)
                        .and(communityBoard.deleted.eq(false)))
                .fetchOne());
    }

    @Override
    public List<CommunityBoardWithNickname> getCommunityBoards() {
        QCommunityBoard communityBoard = QCommunityBoard.communityBoard;
        QVolunteer volunteer = QVolunteer.volunteer;

        return queryFactory
                .select(Projections.constructor(CommunityBoardWithNickname.class,
                        communityBoard,
                        volunteer.nickname))
                .from(communityBoard)
                .join(volunteer).on(communityBoard.writerId.eq(volunteer.id))
                .where(communityBoard.deleted.eq(false))
                .orderBy(communityBoard.createdAt.desc())
                .fetch();
    }

    @Override
    public List<CommunityBoardWithNickname> findByWriterId(UUID writerId) {
        QCommunityBoard communityBoard = QCommunityBoard.communityBoard;
        QVolunteer volunteer = QVolunteer.volunteer;

        return queryFactory
                .select(Projections.constructor(CommunityBoardWithNickname.class,
                        communityBoard,
                        volunteer.nickname))
                .from(communityBoard)
                .join(volunteer).on(communityBoard.writerId.eq(volunteer.id))
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
