package com.somemore.domains.volunteer.repository;

import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.repository.mapper.VolunteerOverviewForRankingByHours;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VolunteerRepository {

    Volunteer save(Volunteer volunteer);

    Optional<Volunteer> findById(UUID id);

    Optional<Volunteer> findByOauthId(String oauthId);

    String findNicknameById(UUID id);

    List<VolunteerOverviewForRankingByHours> findRankingByVolunteerHours();

    void deleteAllInBatch();

    List<Volunteer> findAllByIds(List<UUID> volunteerIds);

    boolean existsByVolunteerId(UUID volunteerId);

    default boolean doesNotExistsByVolunteerId(UUID volunteerId) {
        return !existsByVolunteerId(volunteerId);
    }
}
