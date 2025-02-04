package com.somemore.domains.volunteerapply.service;

import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import com.somemore.domains.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import com.somemore.global.exception.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER_APPLY;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VolunteerApplyQueryService implements VolunteerApplyQueryUseCase {

    private final VolunteerApplyRepository volunteerApplyRepository;

    @Override
    public VolunteerApply getById(Long id) {
        return volunteerApplyRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(NOT_EXISTS_VOLUNTEER_APPLY));
    }

    @Override
    public VolunteerApply getByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId) {
        return volunteerApplyRepository.findByRecruitIdAndVolunteerId(recruitId, volunteerId).orElseThrow(
                () -> new NoSuchElementException(NOT_EXISTS_VOLUNTEER_APPLY));
    }

    @Override
    public Long getRecruitBoardIdById(Long id) {
        return volunteerApplyRepository.findRecruitBoardIdById(id).orElseThrow(
                () -> new NoSuchElementException(NOT_EXISTS_VOLUNTEER_APPLY)
        );
    }

    @Override
    public VolunteerApplySummaryResponseDto getSummaryByRecruitId(Long recruitId) {

        List<VolunteerApply> applies = volunteerApplyRepository.findAllByRecruitId(recruitId);

        return VolunteerApplySummaryResponseDto.from(applies);
    }

    @Override
    public Page<VolunteerApply> getAllByRecruitId(Long recruitId, VolunteerApplySearchCondition condition) {
        return volunteerApplyRepository.findAllByRecruitId(recruitId, condition);
    }

    @Override
    public Page<VolunteerApply> getAllByVolunteerId(UUID volunteerId, VolunteerApplySearchCondition condition) {
        return volunteerApplyRepository.findAllByVolunteerId(volunteerId, condition);
    }

    @Override
    public List<UUID> getVolunteerIdsByRecruitIds(List<Long> recruitIds) {
        return volunteerApplyRepository.findVolunteerIdsByRecruitIds(recruitIds);
    }

    @Override
    public List<VolunteerApply> getAllByIds(List<Long> ids) {
        return volunteerApplyRepository.findAllByIds(ids);
    }

}
