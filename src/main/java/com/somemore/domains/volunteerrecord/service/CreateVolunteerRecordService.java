package com.somemore.domains.volunteerrecord.service;

import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import com.somemore.domains.volunteerrecord.repository.VolunteerRecordRepository;
import com.somemore.domains.volunteerrecord.usecase.CreateVolunteerRecordUseCase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateVolunteerRecordService implements CreateVolunteerRecordUseCase {

    private final VolunteerRecordRepository volunteerRecordRepository;

    public void create(VolunteerRecord volunteerRecord) {
        volunteerRecordRepository.save(volunteerRecord);
    }
}
