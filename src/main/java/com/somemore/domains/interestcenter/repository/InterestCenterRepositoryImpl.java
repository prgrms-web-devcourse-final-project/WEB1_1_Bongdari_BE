package com.somemore.domains.interestcenter.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.domains.interestcenter.domain.QInterestCenter;
import com.somemore.domains.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import com.somemore.domains.interestcenter.domain.InterestCenter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InterestCenterRepositoryImpl implements InterestCenterRepository {

    private final JPAQueryFactory queryFactory;
    private final InterestCenterJpaRepository interestCenterJpaRepository;

    private final static QInterestCenter interestCenter = QInterestCenter.interestCenter;

    @Override
    public InterestCenter save(InterestCenter interestCenter) {
        return interestCenterJpaRepository.save(interestCenter);
    }

    @Override
    public Optional<InterestCenter> findById(Long id) {

        InterestCenter result = queryFactory
                .selectFrom(interestCenter)
                .where(
                        interestCenter.id.eq(id)
                                .and(isNotDeleted())
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<RegisterInterestCenterResponseDto> findInterestCenterResponseById(Long id) {

        RegisterInterestCenterResponseDto result = queryFactory
                .select(
                        Projections.constructor(
                                RegisterInterestCenterResponseDto.class,
                                interestCenter.id,
                                interestCenter.volunteerId,
                                interestCenter.centerId
                        )
                )
                .from(interestCenter)
                .where(
                        interestCenter.id.eq(id)
                                .and(isNotDeleted())
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<UUID> findInterestCenterIdsByVolunteerId(UUID volunteerId) {

        return queryFactory
                .select(interestCenter.centerId)
                .from(interestCenter)
                .where(
                        interestCenter.volunteerId.eq(volunteerId)
                                .and(isNotDeleted())
                )
                .fetch();
    }

    @Override
    public List<UUID> findVolunteerIdsByCenterId(UUID centerId) {

        return queryFactory
                .select(interestCenter.volunteerId)
                .from(interestCenter)
                .where(
                        interestCenter.centerId.eq(centerId),
                        isNotDeleted()
                )
                .fetch();
    }

    @Override
    public boolean existsByVolunteerIdAndCenterId(UUID volunteerId, UUID centerId) {

        Integer result = queryFactory
                .selectOne()
                .from(interestCenter)
                .where(
                        interestCenter.volunteerId.eq(volunteerId)
                                .and(interestCenter.centerId.eq(centerId))
                                .and(isNotDeleted())
                )
                .fetchFirst();

        return result != null;
    }


    @Override
    public Optional<InterestCenter> findByVolunteerIdAndCenterId(UUID volunteerId, UUID centerId) {

        InterestCenter result = queryFactory.selectFrom(interestCenter)
                .where(
                        interestCenter.volunteerId.eq(volunteerId)
                                .and(interestCenter.centerId.eq(centerId))
                                .and(interestCenter.deleted.isFalse()))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    private static BooleanExpression isNotDeleted() {
        return interestCenter.deleted.eq(false);
    }
}
