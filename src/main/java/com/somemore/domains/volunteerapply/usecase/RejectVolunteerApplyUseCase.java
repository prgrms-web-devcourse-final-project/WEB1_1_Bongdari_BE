package com.somemore.domains.volunteerapply.usecase;

import java.util.UUID;

public interface RejectVolunteerApplyUseCase {

    void reject(Long id, UUID centerId);

}
