package com.somemore.volunteer.usecase;

import com.somemore.volunteer.domain.Volunteer_NEW;

import java.util.UUID;

public interface RegisterVolunteerUseCase {

    Volunteer_NEW register(UUID userId);
}
