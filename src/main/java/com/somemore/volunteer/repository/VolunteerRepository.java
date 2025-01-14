package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.Volunteer_NEW;

public interface VolunteerRepository {

    Volunteer_NEW save(Volunteer_NEW volunteer);
}
