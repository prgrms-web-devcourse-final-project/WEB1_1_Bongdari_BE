package com.somemore.domains.volunteerapply.usecase;

import com.somemore.domains.volunteerapply.dto.request.VolunteerApplyCreateRequestDto;
import java.util.UUID;

public interface ApplyVolunteerApplyUseCase {

    Long apply(VolunteerApplyCreateRequestDto requestDto, UUID volunteerId);

}
