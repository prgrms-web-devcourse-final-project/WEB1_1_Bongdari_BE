package com.somemore.volunteer.repository;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.volunteer.domain.QVolunteer;
import com.somemore.volunteer.domain.Volunteer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class VolunteerRepositoryImpl implements VolunteerRepository {

    private final VolunteerJpaRepository volunteerJpaRepository;
    private final JPAQueryFactory queryFactory;

    private static final QVolunteer volunteer = QVolunteer.volunteer;

    @Override
    public Volunteer save(Volunteer volunteer) {
        return volunteerJpaRepository.save(volunteer);
    }

    @Override
    public Optional<Volunteer> findById(UUID id) {
        return findOne(volunteer.id.eq(id));
    }

    @Override
    public Optional<Volunteer> findByOauthId(String oauthId) {
        return findOne(volunteer.oauthId.eq(oauthId));
    }

    @Override
    public String findNicknameById(UUID id) {
        return findDynamicField(id, volunteer.nickname)
                .orElse(null);
    }

    @Override
    public void deleteAllInBatch() {
        volunteerJpaRepository.deleteAllInBatch();
    }

    private Optional<Volunteer> findOne(BooleanExpression condition) {

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(volunteer)
                        .where(
                                condition,
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    private <T> Optional<T> findDynamicField(UUID id, Path<T> field) {

        return Optional.ofNullable(
                queryFactory
                        .select(field)
                        .from(volunteer)
                        .where(
                                volunteer.id.eq(id),
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    private BooleanExpression isNotDeleted() {
        return volunteer.deleted.isFalse();
    }
}