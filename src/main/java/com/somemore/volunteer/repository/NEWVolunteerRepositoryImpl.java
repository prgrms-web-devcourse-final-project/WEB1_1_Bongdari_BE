package com.somemore.volunteer.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.domain.QNEWVolunteer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("newVolunteerRepository")
@RequiredArgsConstructor
public class NEWVolunteerRepositoryImpl implements NEWVolunteerRepository {

    private final NEWVolunteerJpaRepository NEWVolunteerJpaRepository;
    private final JPAQueryFactory queryFactory;

    private static final QNEWVolunteer volunteer = QNEWVolunteer.nEWVolunteer;

    @Override
    public NEWVolunteer save(NEWVolunteer volunteer) {
        return NEWVolunteerJpaRepository.save(volunteer);
    }

    @Override
    public Optional<NEWVolunteer> findById(UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(volunteer)
                        .where(
                                volunteer.id.eq(id),
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    @Override
    public Optional<NEWVolunteer> findByUserId(UUID userId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(volunteer)
                        .where(
                                volunteer.userId.eq(userId),
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<String> findNicknamesByIds(List<UUID> ids) {
        return queryFactory
                .select(volunteer.nickname)
                .from(volunteer)
                .where(
                        volunteer.id.in(ids),
                        isNotDeleted()
                )
                .fetch(); // 결과를 리스트로 반환
    }

    private static BooleanExpression isNotDeleted() {
        return volunteer.deleted.eq(false);
    }
}
