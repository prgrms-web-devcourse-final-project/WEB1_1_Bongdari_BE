package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.Volunteer;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VolunteerRepository {
    Volunteer save(Volunteer volunteer);
    Optional<Volunteer> findById(UUID id);
    Optional<Volunteer> findByOauthId(String oauthId);
    String findNicknameById(UUID id);
    void deleteAllInBatch();
}
