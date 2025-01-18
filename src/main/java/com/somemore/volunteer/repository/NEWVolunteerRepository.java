package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.record.VolunteerNickname;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NEWVolunteerRepository {

    NEWVolunteer save(NEWVolunteer volunteer);

    Optional<NEWVolunteer> findById(UUID id);

    Optional<NEWVolunteer> findByUserId(UUID userId);

    List<VolunteerNickname> findNicknamesByIds(List<UUID> ids);
}
