package com.somemore.location.usecase.command;

import com.somemore.location.dto.request.LocationCreateRequestDto;

public interface CreateLocationUseCase {

    Long createLocation(LocationCreateRequestDto requestDto);

}
