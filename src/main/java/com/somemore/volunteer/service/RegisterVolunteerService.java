package com.somemore.volunteer.service;

import com.somemore.volunteer.domain.Volunteer_NEW;
import com.somemore.volunteer.repository.VolunteerRepository;
import com.somemore.volunteer.usecase.RegisterVolunteerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterVolunteerService implements RegisterVolunteerUseCase {

    private final VolunteerRepository volunteerRepository;

    @Override
    public Volunteer_NEW register(UUID userId) {
        Volunteer_NEW volunteer = Volunteer_NEW.createDefault(userId);

        return volunteerRepository.save(volunteer);
    }
}
