package com.somemore.domains.volunteer.usecase;

import com.somemore.domains.volunteer.dto.request.VolunteerRegisterRequestDto;

public interface RegisterVolunteerUseCase {
    void registerVolunteer(VolunteerRegisterRequestDto dto);
}
