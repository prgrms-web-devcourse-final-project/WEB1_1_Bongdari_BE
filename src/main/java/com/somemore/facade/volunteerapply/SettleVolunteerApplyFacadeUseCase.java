package com.somemore.facade.volunteerapply;

import com.somemore.volunteerapply.dto.request.VolunteerApplySettleRequestDto;
import java.util.UUID;

public interface SettleVolunteerApplyFacadeUseCase {

    void settleVolunteerApplies(VolunteerApplySettleRequestDto dto, UUID centerId);
}
