package com.somemore.volunteer.service;

import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.domain.VolunteerDetail;
import com.somemore.volunteer.dto.request.VolunteerRegisterRequestDto;
import com.somemore.volunteer.repository.VolunteerDetailJpaRepository;
import com.somemore.volunteer.repository.VolunteerDetailRepository;
import com.somemore.volunteer.repository.VolunteerRepository;
import com.somemore.volunteer.usecase.RegisterVolunteerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.somemore.volunteer.domain.Volunteer.createDefault;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RegisterVolunteerService implements RegisterVolunteerUseCase {

    private final VolunteerRepository volunteerRepository;
    private final VolunteerDetailRepository volunteerDetailRepository;

    @Override
    public void registerVolunteer(VolunteerRegisterRequestDto dto) {
        Volunteer volunteer = createDefault(dto.oAuthProvider(), dto.oauthId());
        volunteerRepository.save(volunteer);

        VolunteerDetail volunteerDetail = VolunteerDetail.of(dto, volunteer.getId());
        volunteerDetailRepository.save(volunteerDetail);
    }
}
