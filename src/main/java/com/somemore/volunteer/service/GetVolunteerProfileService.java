package com.somemore.volunteer.service;

import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.usecase.UserQueryUseCase;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.dto.VolunteerProfileResponseDto;
import com.somemore.volunteer.usecase.GetVolunteerProfileUseCase;
import com.somemore.volunteer.usecase.NEWVolunteerQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetVolunteerProfileService implements GetVolunteerProfileUseCase {

    private final NEWVolunteerQueryUseCase volunteerQueryUseCase;
    private final UserQueryUseCase userQueryUseCase;

    @Override
    public VolunteerProfileResponseDto getProfile(UUID userId) {
        NEWVolunteer volunteer = volunteerQueryUseCase.getByUserId(userId);
        UserCommonAttribute commonAttribute = userQueryUseCase.getCommonAttributeByUserId(userId);

        return VolunteerProfileResponseDto.of(volunteer, commonAttribute);
    }
}
