package com.somemore.volunteer.usecase;

import com.somemore.volunteer.domain.NEWVolunteer;

import java.util.UUID;

public interface NEWVolunteerQueryUseCase {

    NEWVolunteer getById(UUID id);

    NEWVolunteer getByUserId(UUID userId);

    UUID getUserIdById(UUID id);

    UUID getIdByUserId(UUID userId);
}
