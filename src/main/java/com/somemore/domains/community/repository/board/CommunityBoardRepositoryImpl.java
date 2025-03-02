package com.somemore.domains.community.repository.board;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.domain.QCommunityBoard;
import com.somemore.domains.community.repository.mapper.CommunityBoardView;
import com.somemore.volunteer.domain.QNEWVolunteer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
    private static final QNEWVolunteer volunteer = QNEWVolunteer.nEWVolunteer;

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
    public Page<CommunityBoardView> findCommunityBoards(String keyword, Pageable pageable) {
        List<CommunityBoardView> content = getCommunityBoardsQuery()
                .where(isNotDeleted()
                        .and(keywordEq(keyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(communityBoard.count())
                .from(communityBoard)
                .join(volunteer).on(communityBoard.writerId.eq(volunteer.id))
                .where(isNotDeleted()
                        .and(keywordEq(keyword)));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<CommunityBoardView> findByWriterId(UUID writerId, Pageable pageable) {
        List<CommunityBoardView> content = getCommunityBoardsQuery()
                .where(isWriter(writerId)
                        .and(isNotDeleted()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(communityBoard.count())
                .from(communityBoard)
                .join(volunteer).on(communityBoard.writerId.eq(volunteer.id))
                .where(isWriter(writerId)
                        .and(isNotDeleted()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public boolean existsById(Long id) {
        return communityBoardJpaRepository.existsByIdAndDeletedFalse(id);
    }

    @Override
    public void deleteAllInBatch() {
        communityBoardJpaRepository.deleteAllInBatch();
    }

    @Override
    public List<CommunityBoard> findAllByDeletedFalse() {
        return communityBoardJpaRepository.findAllByDeletedFalse();
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

    private BooleanExpression isNotDeleted() {
        return communityBoard.deleted.eq(false);
    }

    private BooleanExpression isWriter(UUID writerId) {
        return communityBoard.writerId.eq(writerId);
    }

    private BooleanExpression keywordEq(String keyword) {
        return StringUtils.isNotBlank(keyword)
                ? communityBoard.title.containsIgnoreCase(keyword)
                : null;
    }
}
