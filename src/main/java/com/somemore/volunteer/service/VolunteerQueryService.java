package com.somemore.volunteer.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER;

import com.somemore.facade.validator.VolunteerDetailAccessValidator;
import com.somemore.global.exception.BadRequestException;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.domain.VolunteerDetail;
import com.somemore.volunteer.dto.response.VolunteerProfileResponseDto;
import com.somemore.volunteer.dto.response.VolunteerRankingResponseDto;
import com.somemore.volunteer.repository.VolunteerDetailRepository;
import com.somemore.volunteer.repository.VolunteerRepository;
import com.somemore.volunteer.repository.mapper.VolunteerOverviewForRankingByHours;
import com.somemore.volunteer.repository.mapper.VolunteerSimpleInfo;
import com.somemore.volunteer.usecase.VolunteerQueryUseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VolunteerQueryService implements VolunteerQueryUseCase {

    private final VolunteerRepository volunteerRepository;
    private final VolunteerDetailRepository volunteerDetailRepository;
    private final VolunteerDetailAccessValidator volunteerDetailAccessValidator;

    @Override
    public VolunteerProfileResponseDto getMyProfile(UUID volunteerId) {

        return VolunteerProfileResponseDto.from(
                findVolunteer(volunteerId),
                findVolunteerDetail(volunteerId)
        );
    }

    @Override
    public VolunteerProfileResponseDto getVolunteerProfile(UUID volunteerId) {

        return VolunteerProfileResponseDto.from(
                findVolunteer(volunteerId)
        );
    }

    @Override
    public VolunteerProfileResponseDto getVolunteerDetailedProfile(UUID volunteerId,
            UUID centerId) {
        volunteerDetailAccessValidator.validateByCenterId(centerId, volunteerId);

        return VolunteerProfileResponseDto.from(
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

    @Override
    public VolunteerRankingResponseDto getRankingByHours() {
        List<VolunteerOverviewForRankingByHours> rankingByVolunteerHours = volunteerRepository.findRankingByVolunteerHours();
        return VolunteerRankingResponseDto.from(rankingByVolunteerHours);
    }

    @Override
    public List<Volunteer> getAllByIds(List<UUID> volunteerIds) {
        return volunteerRepository.findAllByIds(volunteerIds);
    }

    @Override
    public List<VolunteerSimpleInfo> getVolunteerSimpleInfosByIds(List<UUID> ids) {
        return volunteerRepository.findSimpleInfoByIds(ids);
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
