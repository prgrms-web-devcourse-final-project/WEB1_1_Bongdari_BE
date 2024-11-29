package com.somemore.interestcenter.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.interestcenter.domain.InterestCenter;
import com.somemore.interestcenter.domain.QInterestCenter;
import com.somemore.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class InterestCenterRepositoryImpl implements InterestCenterRepository {

    private final JPAQueryFactory queryFactory;
    private final InterestCenterJpaRepository interestCenterJpaRepository;

    @Override
    public InterestCenter save(InterestCenter interestCenter) {
        return interestCenterJpaRepository.save(interestCenter);
    }

    @Override
    public Optional<InterestCenter> findById(Long id) {
        QInterestCenter interestCenter = QInterestCenter.interestCenter;

        InterestCenter result = queryFactory
                .selectFrom(interestCenter)
                .where(
                        interestCenter.id.eq(id)
                                .and(interestCenter.deleted.eq(false))
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<RegisterInterestCenterResponseDto> findInterestCenterResponseById(Long id) {
        QInterestCenter interestCenter = QInterestCenter.interestCenter;

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
                                .and(interestCenter.deleted.eq(false))
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsByVolunteerIdAndCenterId(UUID volunteerId, UUID centerId) {
        QInterestCenter interestCenter = QInterestCenter.interestCenter;

        Integer result = queryFactory
                .selectOne()
                .from(interestCenter)
                .where(
                        interestCenter.volunteerId.eq(volunteerId)
                                .and(interestCenter.centerId.eq(centerId))
                                .and(interestCenter.deleted.eq(false))
                )
                .fetchFirst();

        return result != null;
    }

}
