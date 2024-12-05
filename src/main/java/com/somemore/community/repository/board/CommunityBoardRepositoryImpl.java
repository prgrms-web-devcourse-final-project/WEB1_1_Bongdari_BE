package com.somemore.community.repository.board;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.repository.mapper.CommunityBoardView;
import com.somemore.community.domain.QCommunityBoard;
import com.somemore.volunteer.domain.QVolunteer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CommunityBoardRepositoryImpl implements CommunityBoardRepository {

    private final JPAQueryFactory queryFactory;
    private final CommunityBoardJpaRepository communityBoardJpaRepository;

    private static final QCommunityBoard communityBoard = QCommunityBoard.communityBoard;
    private static final QVolunteer volunteer = QVolunteer.volunteer;

    @Override
    public CommunityBoard save(CommunityBoard communityBoard) {
        return communityBoardJpaRepository.save(communityBoard);
    }

    @Override
    public Optional<CommunityBoard> findById(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(communityBoard)
                .where(communityBoard.id.eq(id)
                        .and(isNotDeleted()))
                .fetchOne());
    }

    @Override
    public Page<CommunityBoardView> findCommunityBoards(Pageable pageable) {
        List<CommunityBoardView> content = getCommunityBoardsQuery()
                .where(QCommunityBoard.communityBoard.deleted.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(communityBoard.count())
                .from(communityBoard)
                .where(isNotDeleted());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<CommunityBoardView> findByWriterId(UUID writerId, Pageable pageable) {
        List<CommunityBoardView> content = getCommunityBoardsQuery()
                .where(QCommunityBoard.communityBoard.writerId.eq(writerId)
                        .and(QCommunityBoard.communityBoard.deleted.eq(false)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(communityBoard.count())
                .from(communityBoard)
                .where(QCommunityBoard.communityBoard.writerId.eq(writerId)
                        .and(isNotDeleted()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public boolean existsById(Long id) {
        return communityBoardJpaRepository.existsByIdAndDeletedFalse(id);
    }

    private JPAQuery<CommunityBoardView> getCommunityBoardsQuery() {
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

    private BooleanExpression isNotDeleted() {
        return communityBoard.deleted.eq(false);
    }
}
