package com.somemore.domains.location.usecase.command;

import com.somemore.domains.location.dto.request.LocationUpdateRequestDto;

public interface UpdateLocationUseCase {

    void updateLocation(LocationUpdateRequestDto requestDto, Long locationId);
}
