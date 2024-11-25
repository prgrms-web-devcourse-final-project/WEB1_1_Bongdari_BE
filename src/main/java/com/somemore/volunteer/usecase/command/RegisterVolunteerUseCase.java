package com.somemore.volunteer.usecase.command;

import com.somemore.volunteer.dto.request.VolunteerRegisterRequestDto;

public interface RegisterVolunteerUseCase {
    void registerVolunteer(VolunteerRegisterRequestDto dto);
}
