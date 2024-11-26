package com.somemore.location.usecase.command;

import com.somemore.location.dto.request.LocationUpdateRequestDto;

public interface UpdateLocationUseCase {

    void updateLocation(LocationUpdateRequestDto requestDto, Long locationId);
}
