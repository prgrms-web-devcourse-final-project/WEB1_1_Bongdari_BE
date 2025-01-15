package com.somemore.volunteer.service;

import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import com.somemore.volunteer.usecase.NEWRegisterVolunteerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NEWRegisterVolunteerService implements NEWRegisterVolunteerUseCase {

    @Qualifier("newVolunteerRepository")
    private final NEWVolunteerRepository NEWVolunteerRepository;

    @Override
    public NEWVolunteer register(UUID userId) {
        NEWVolunteer volunteer = NEWVolunteer.createDefault(userId);

        return NEWVolunteerRepository.save(volunteer);
    }
}
