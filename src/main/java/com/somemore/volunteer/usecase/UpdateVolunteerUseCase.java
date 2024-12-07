package com.somemore.volunteer.usecase;

import java.util.UUID;

public interface UpdateVolunteerUseCase {

    void updateVolunteerStats(UUID id, int hours);

}
