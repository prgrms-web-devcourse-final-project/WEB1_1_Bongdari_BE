package com.somemore.volunteerapply.usecase;

import com.somemore.volunteerapply.dto.VolunteerApplyCreateRequestDto;
import java.util.UUID;

public interface VolunteerApplyCommandUseCase {

    Long apply(VolunteerApplyCreateRequestDto requestDto, UUID volunteerId);

}
