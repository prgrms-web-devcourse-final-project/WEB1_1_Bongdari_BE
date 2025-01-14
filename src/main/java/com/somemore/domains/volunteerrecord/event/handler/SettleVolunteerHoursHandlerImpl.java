package com.somemore.domains.volunteerrecord.event.handler;

import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import com.somemore.domains.volunteerrecord.usecase.CreateVolunteerRecordUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class SettleVolunteerHoursHandlerImpl implements SettleVolunteerHoursHandler {

    private final CreateVolunteerRecordUseCase createvolunteerRecordUseCase;

    @Override
    public void handle(VolunteerRecord volunteerRecord) {

        createvolunteerRecordUseCase.create(volunteerRecord);
    }
}
