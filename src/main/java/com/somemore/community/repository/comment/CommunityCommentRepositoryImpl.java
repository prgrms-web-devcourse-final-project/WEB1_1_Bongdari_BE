package com.somemore.community.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.community.domain.CommunityComment;
import com.somemore.community.domain.QCommunityComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CommunityCommentRepositoryImpl implements CommunityCommentRepository {

    private final JPAQueryFactory queryFactory;
    private final CommunityCommentJpaRepository communityCommentJpaRepository;

    @Override
    public CommunityComment save(CommunityComment communityComment) {
        return communityCommentJpaRepository.save(communityComment);
    }

    @Override
    public Optional<CommunityComment> findById(Long id) {
        QCommunityComment communityComment = QCommunityComment.communityComment;

        return Optional.ofNullable(queryFactory
                .selectFrom(communityComment)
                .where(communityComment.id.eq(id)
                        .and(communityComment.deleted.eq(false)))
                .fetchOne());
    }

    @Override
    public boolean existsById(Long id) {
        QCommunityComment communityComment = QCommunityComment.communityComment;

        return queryFactory
                .selectOne()
                .from(communityComment)
                .where(communityComment.id.eq(id)
                        .and(communityComment.deleted.eq(false)))
                .fetchFirst() != null;
    }

    @Override
    public void deleteAllInBatch() { communityCommentJpaRepository.deleteAllInBatch(); }
}
