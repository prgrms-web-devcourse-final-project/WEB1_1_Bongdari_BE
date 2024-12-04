package com.somemore.volunteerapply.usecase;

import com.somemore.volunteerapply.dto.VolunteerApplyCreateRequestDto;
import java.util.UUID;

public interface ApplyVolunteerApplyUseCase {

    Long apply(VolunteerApplyCreateRequestDto requestDto, UUID volunteerId);

}
