package com.somemore.volunteerapply.usecase;

import com.somemore.volunteerapply.dto.request.VolunteerApplySettleRequestDto;
import java.util.UUID;

public interface SettleVolunteerApplyFacadeUseCase {

    void settleVolunteerApplies(VolunteerApplySettleRequestDto dto, UUID centerId);
}
