package com.somemore.volunteer.service;

import com.somemore.facade.validator.VolunteerDetailAccessValidatorImpl;
import com.somemore.global.exception.BadRequestException;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.domain.VolunteerDetail;
import com.somemore.volunteer.dto.response.VolunteerResponseDto;
import com.somemore.volunteer.repository.VolunteerDetailRepository;
import com.somemore.volunteer.repository.VolunteerRepository;
import com.somemore.volunteer.usecase.VolunteerQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VolunteerQueryService implements VolunteerQueryUseCase {

    private final VolunteerRepository volunteerRepository;
    private final VolunteerDetailRepository volunteerDetailRepository;
    private final VolunteerDetailAccessValidatorImpl volunteerDetailAccessValidatorImpl;

    @Override
    public VolunteerResponseDto getMyProfile(UUID volunteerId) {

        return VolunteerResponseDto.from(
                findVolunteer(volunteerId),
                findVolunteerDetail(volunteerId)
        );
    }

    @Override
    public VolunteerResponseDto getVolunteerProfile(UUID volunteerId) {

        return VolunteerResponseDto.from(
                findVolunteer(volunteerId)
        );
    }

    @Override
    public VolunteerResponseDto getVolunteerDetailedProfile(UUID volunteerId, UUID centerId) {
        volunteerDetailAccessValidatorImpl.validateByCenterId(centerId, volunteerId);

        return VolunteerResponseDto.from(
                findVolunteer(volunteerId),
                findVolunteerDetail(volunteerId)
        );
    }

    @Override
    public UUID getVolunteerIdByOAuthId(String oAuthId) {
        return volunteerRepository.findByOauthId(oAuthId)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_VOLUNTEER))
                .getId();
    }

    @Override
    public String getNicknameById(UUID id) {
        String nickname = volunteerRepository.findNicknameById(id);

        if (nickname == null || nickname.isBlank()) {
            throw new BadRequestException(NOT_EXISTS_VOLUNTEER);
        }

        return nickname;
    }

    private Volunteer findVolunteer(UUID volunteerId) {
        return volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_VOLUNTEER));

    }

    private VolunteerDetail findVolunteerDetail(UUID volunteerId) {
        return volunteerDetailRepository.findByVolunteerId(volunteerId)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_VOLUNTEER));
    }
}
