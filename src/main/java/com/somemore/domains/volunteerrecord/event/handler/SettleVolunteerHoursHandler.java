package com.somemore.domains.volunteerrecord.event.handler;

import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;

public interface SettleVolunteerHoursHandler {

    void handle(VolunteerRecord volunteerRecord);

}
