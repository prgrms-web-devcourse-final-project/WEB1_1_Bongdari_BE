package com.somemore.volunteer.service;

import com.somemore.volunteer.domain.Volunteer_NEW;
import com.somemore.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class registerVolunteerService implements registerVolunteerUseCase {

    private final VolunteerRepository volunteerRepository;

    @Override
    public Volunteer_NEW register(UUID userId) {
        Volunteer_NEW volunteer = Volunteer_NEW.createDefault(userId);

        return volunteerRepository.save(volunteer);
    }
}
