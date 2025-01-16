package com.somemore.domains.volunteer.service;

import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.domain.VolunteerDetail;
import com.somemore.domains.volunteer.dto.request.VolunteerRegisterRequestDto;
import com.somemore.domains.volunteer.repository.VolunteerDetailRepository;
import com.somemore.domains.volunteer.repository.VolunteerRepository;
import com.somemore.domains.volunteer.usecase.RegisterVolunteerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RegisterVolunteerService implements RegisterVolunteerUseCase {

    private final VolunteerRepository volunteerRepository;
    private final VolunteerDetailRepository volunteerDetailRepository;

    @Override
    public void registerVolunteer(VolunteerRegisterRequestDto dto) {
        Volunteer volunteer = Volunteer.createDefault(dto.oAuthProvider(), dto.oauthId());
        volunteerRepository.save(volunteer);

        VolunteerDetail volunteerDetail = VolunteerDetail.of(dto, volunteer.getId());
        volunteerDetailRepository.save(volunteerDetail);
    }
}
