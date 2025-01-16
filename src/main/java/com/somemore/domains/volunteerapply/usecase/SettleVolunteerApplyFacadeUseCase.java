package com.somemore.domains.volunteerapply.usecase;

import com.somemore.domains.volunteerapply.dto.request.VolunteerApplySettleRequestDto;

import java.util.UUID;

public interface SettleVolunteerApplyFacadeUseCase {

    void settleVolunteerApplies(VolunteerApplySettleRequestDto dto, UUID centerId);
}
