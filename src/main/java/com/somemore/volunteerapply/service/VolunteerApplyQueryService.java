package com.somemore.volunteerapply.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER_APPLY;

import com.somemore.global.exception.BadRequestException;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VolunteerApplyQueryService implements VolunteerApplyQueryUseCase {

    private final VolunteerApplyRepository volunteerApplyRepository;

    @Override
    public List<UUID> getVolunteerIdsByRecruitIds(List<Long> recruitIds) {

        return volunteerApplyRepository.findVolunteerIdsByRecruitIds(recruitIds);
    }

    @Override
    public VolunteerApply getByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId) {
        return getVolunteerApplyBy(recruitId, volunteerId);
    }

    private VolunteerApply getVolunteerApplyBy(Long recruitBoardId, UUID volunteerId) {
        return volunteerApplyRepository.findByRecruitIdAndVolunteerId(recruitBoardId,
                volunteerId).orElseThrow(
                () -> new BadRequestException(NOT_EXISTS_VOLUNTEER_APPLY.getMessage()));
    }
}
