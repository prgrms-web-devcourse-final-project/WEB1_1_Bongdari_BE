package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.Volunteer;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VolunteerRepository {
    Volunteer save(Volunteer volunteer);
    String findNicknameById(UUID id);
    Optional<Volunteer> findByOauthId(String oauthId);
    void deleteAllInBatch();
}
