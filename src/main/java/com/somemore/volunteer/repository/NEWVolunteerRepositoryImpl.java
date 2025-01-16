package com.somemore.volunteer.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.domain.QNEWVolunteer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("newVolunteerRepository")
@RequiredArgsConstructor
public class NEWVolunteerRepositoryImpl implements NEWVolunteerRepository {

    @Qualifier("newVolunteerJpaRepository")
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
        );    }

    private static BooleanExpression isNotDeleted() {
        return volunteer.deleted.eq(false);
    }
}
