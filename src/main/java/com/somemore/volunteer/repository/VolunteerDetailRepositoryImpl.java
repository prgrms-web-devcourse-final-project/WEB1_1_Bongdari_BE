package com.somemore.volunteer.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.volunteer.domain.QVolunteerDetail;
import com.somemore.volunteer.domain.VolunteerDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class VolunteerDetailRepositoryImpl implements VolunteerDetailRepository {

    private final VolunteerDetailJpaRepository volunteerDetailJpaRepository;
    private final JPAQueryFactory queryFactory;

    private final QVolunteerDetail volunteerDetail = QVolunteerDetail.volunteerDetail;

    @Override
    public VolunteerDetail save(VolunteerDetail volunteerDetail) {
        return volunteerDetailJpaRepository.save(volunteerDetail);
    }

    @Override
    public Optional<VolunteerDetail> findByVolunteerId(UUID volunteerId) {
        return findOne(volunteerDetail.volunteerId.eq(volunteerId));
    }

    private Optional<VolunteerDetail> findOne(BooleanExpression condition) {
        VolunteerDetail result = queryFactory
                .selectFrom(volunteerDetail)
                .where(
                        condition,
                        isNotDeleted()
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    private BooleanExpression isNotDeleted() {
        return volunteerDetail.deleted.isFalse();
    }
}
