package com.somemore.volunteer.usecase;

import com.somemore.volunteer.domain.NEWVolunteer;

import java.util.UUID;

public interface NEWVolunteerQueryUseCase {

    NEWVolunteer getByUserId(UUID userId);

    UUID getIdByUserId(UUID userId);
}
