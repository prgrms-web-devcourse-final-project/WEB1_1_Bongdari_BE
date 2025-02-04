package com.somemore.volunteer.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.domain.QNEWVolunteer;
import com.somemore.volunteer.repository.record.VolunteerNickname;
import com.somemore.volunteer.repository.record.VolunteerNicknameAndId;
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
    public Optional<String> findNicknameById(UUID id) {
        return Optional.ofNullable(
                queryFactory.select(volunteer.nickname)
                        .from(volunteer)
                        .where(
                                volunteer.id.eq(id),
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<VolunteerNickname> findNicknamesByIds(List<UUID> ids) {

        return queryFactory
                .select(Projections.constructor(VolunteerNickname.class,
                        volunteer.id,
                        volunteer.nickname))
                .from(volunteer)
                .where(
                        inIds(ids),
                        isNotDeleted()
                )
                .fetch();
    }

    @Override
    public List<VolunteerNicknameAndId> findVolunteerNicknameAndIdsByIds(List<UUID> ids) {
        return queryFactory
                .select(Projections.constructor(VolunteerNicknameAndId.class,
                        volunteer.id,
                        volunteer.userId,
                        volunteer.nickname))
                .from(volunteer)
                .where(
                        inIds(ids),
                        isNotDeleted()
                )
                .fetch();
    }

    private static BooleanExpression isNotDeleted() {
        return volunteer.deleted.eq(false);
    }

    private static BooleanExpression inIds(List<UUID> ids) {
        return volunteer.id.in(ids);
    }
}
