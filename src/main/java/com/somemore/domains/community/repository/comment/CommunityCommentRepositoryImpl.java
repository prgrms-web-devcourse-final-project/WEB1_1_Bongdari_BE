package com.somemore.domains.community.repository.comment;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.domains.community.domain.CommunityComment;
import com.somemore.domains.community.domain.QCommunityComment;
import com.somemore.domains.community.repository.mapper.CommunityCommentView;
import com.somemore.domains.volunteer.domain.QVolunteer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CommunityCommentRepositoryImpl implements CommunityCommentRepository {

    private final JPAQueryFactory queryFactory;
    private final CommunityCommentJpaRepository communityCommentJpaRepository;

    private static final QCommunityComment communityComment = QCommunityComment.communityComment;
    private static final QVolunteer volunteer = QVolunteer.volunteer;

    @Override
    public CommunityComment save(CommunityComment communityComment) {
        return communityCommentJpaRepository.save(communityComment);
    }

    @Override
    public Optional<CommunityComment> findById(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(communityComment)
                .where(communityComment.id.eq(id)
                        .and(communityComment.deleted.eq(false)))
                .fetchOne());
    }

    public Page<CommunityCommentView> findCommentsByBoardId(Long boardId, Pageable pageable) {
        List<CommunityCommentView> content = queryFactory
                .select(Projections.constructor(CommunityCommentView.class,
                        communityComment,
                        volunteer.nickname))
                .from(communityComment)
                .join(volunteer).on(communityComment.writerId.eq(volunteer.id))
                .where(communityComment.communityBoardId.eq(boardId))
                .orderBy(communityComment.parentCommentId.asc().nullsFirst(), communityComment.createdAt.asc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(communityComment.count())
                .from(communityComment)
                .join(volunteer).on(communityComment.writerId.eq(volunteer.id))
                .where(communityComment.communityBoardId.eq(boardId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public boolean existsById(Long id) {
        return queryFactory
                .selectOne()
                .from(communityComment)
                .where(communityComment.id.eq(id)
                        .and(communityComment.deleted.eq(false)))
                .fetchFirst() != null;
    }

    @Override
    public void deleteAllInBatch() {
        communityCommentJpaRepository.deleteAllInBatch();
    }
}
