package com.somemore.volunteerApply.service;

import com.somemore.volunteerApply.repository.VolunteerApplyRepository;
import com.somemore.volunteerApply.usecase.VolunteerApplyQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
}
