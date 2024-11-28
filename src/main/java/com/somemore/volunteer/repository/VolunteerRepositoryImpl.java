package com.somemore.volunteer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.volunteer.domain.QVolunteer;
import com.somemore.volunteer.domain.Volunteer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class VolunteerRepositoryImpl implements VolunteerRepository{

    private final VolunteerJpaRepository volunteerJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Volunteer save(Volunteer volunteer) {
        return volunteerJpaRepository.save(volunteer);
    }

    @Override
    public String findNicknameById(UUID id) {
        QVolunteer volunteer = QVolunteer.volunteer;

        return queryFactory
                .select(volunteer.nickname)
                .from(volunteer)
                .where(volunteer.id.eq(id))
                .fetchOne();
    }

    @Override
    public Optional<Volunteer> findByOauthId(String oauthId) {
        return volunteerJpaRepository.findByOauthId(oauthId);
    }

    @Override
    public void deleteAllInBatch() {
        volunteerJpaRepository.deleteAllInBatch();
    }
}
