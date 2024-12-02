package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.dto.response.VolunteerRankingResponseDto;
import com.somemore.volunteer.repository.mapper.VolunteerOverviewForRankingByHours;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerRepository {

    Volunteer save(Volunteer volunteer);

    Optional<Volunteer> findById(UUID id);

    Optional<Volunteer> findByOauthId(String oauthId);

    String findNicknameById(UUID id);

    List<VolunteerOverviewForRankingByHours> findRankingByVolunteerHours();

    void deleteAllInBatch();

    List<Volunteer> findAllByIds(List<UUID> volunteerIds);
}
