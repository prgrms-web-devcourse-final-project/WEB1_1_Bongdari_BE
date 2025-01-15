package com.somemore.volunteer.service;

import com.somemore.volunteer.domain.Volunteer_NEW;

import java.util.UUID;

public interface registerVolunteerUseCase {

    Volunteer_NEW register(UUID userId);
}
