package com.somemore.volunteer.usecase;

import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.record.VolunteerNicknameAndId;

import java.util.List;
import java.util.UUID;

public interface NEWVolunteerQueryUseCase {

    NEWVolunteer getById(UUID id);

    NEWVolunteer getByUserId(UUID userId);

    UUID getUserIdById(UUID id);

    UUID getIdByUserId(UUID userId);

    List<VolunteerNicknameAndId> getVolunteerNicknameAndIdsByIds(List<UUID> ids);
}
