package com.somemore.volunteerapply.usecase;

import java.util.UUID;

public interface RejectVolunteerApplyUseCase {

    void reject(Long id, UUID centerId);

}
