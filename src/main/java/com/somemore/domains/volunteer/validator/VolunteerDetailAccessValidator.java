package com.somemore.domains.volunteer.validator;

import java.util.UUID;

public interface VolunteerDetailAccessValidator {

    void validateByCenterId(UUID centerId, UUID targetVolunteerId);
}
