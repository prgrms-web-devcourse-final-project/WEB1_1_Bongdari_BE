package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.record.VolunteerNickname;
import com.somemore.volunteer.repository.record.VolunteerNicknameAndId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NEWVolunteerRepository {

    NEWVolunteer save(NEWVolunteer volunteer);

    Optional<NEWVolunteer> findById(UUID id);

    Optional<NEWVolunteer> findByUserId(UUID userId);

    Optional<String> findNicknameById(UUID id);

    List<VolunteerNickname> findNicknamesByIds(List<UUID> ids);

    List<VolunteerNicknameAndId> findVolunteerNicknameAndIdsByIds(List<UUID> ids);

    boolean existsById(UUID id);

    default boolean doesNotExistById(UUID id) {
        return !existsById(id);
    }
}
