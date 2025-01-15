package com.somemore.volunteer.usecase;

import com.somemore.volunteer.domain.NEWVolunteer;

import java.util.UUID;

public interface NEWRegisterVolunteerUseCase {

    NEWVolunteer register(UUID userId);
}
