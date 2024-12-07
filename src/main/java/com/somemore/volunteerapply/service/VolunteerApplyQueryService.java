package com.somemore.volunteerapply.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER_APPLY;

import com.somemore.global.exception.BadRequestException;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.volunteerapply.dto.response.VolunteerApplyResponseDto;
import com.somemore.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VolunteerApplyQueryService implements VolunteerApplyQueryUseCase {

    private final VolunteerApplyRepository volunteerApplyRepository;

    @Override
    public List<UUID> getVolunteerIdsByRecruitIds(List<Long> recruitIds) {
        return volunteerApplyRepository.findVolunteerIdsByRecruitIds(recruitIds);
    }

    @Override
    public VolunteerApply getByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId) {
        return volunteerApplyRepository.findByRecruitIdAndVolunteerId(recruitId, volunteerId)
                .orElseThrow(
                        () -> new BadRequestException(NOT_EXISTS_VOLUNTEER_APPLY));
    }

    @Override
    public VolunteerApplySummaryResponseDto getSummaryByRecruitId(Long recruitId) {

        List<VolunteerApply> applies = volunteerApplyRepository.findAllByRecruitId(
                recruitId);

        return VolunteerApplySummaryResponseDto.from(applies);
    }

    @Override
    public VolunteerApplyResponseDto getVolunteerApplyByRecruitIdAndVolunteerId(Long recruitId,
            UUID volunteerId) {
        VolunteerApply apply = getByRecruitIdAndVolunteerId(recruitId, volunteerId);

        return VolunteerApplyResponseDto.from(apply);
    }

    @Override
    public Page<VolunteerApply> getAllByRecruitId(Long recruitId,
            VolunteerApplySearchCondition condition) {
        return volunteerApplyRepository.findAllByRecruitId(recruitId, condition);
    }

    @Override
    public Page<VolunteerApply> getAllByVolunteerId(UUID volunteerId,
            VolunteerApplySearchCondition condition) {
        return volunteerApplyRepository.findAllByVolunteerId(volunteerId, condition);
    }

    @Override
    public List<VolunteerApply> getAllByIds(List<Long> ids) {
        return volunteerApplyRepository.findAllByIds(ids);
    }

}
