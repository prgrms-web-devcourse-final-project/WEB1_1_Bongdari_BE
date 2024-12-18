package com.somemore.domains.location.usecase.command;

import com.somemore.domains.location.dto.request.LocationCreateRequestDto;

public interface CreateLocationUseCase {

    Long createLocation(LocationCreateRequestDto requestDto);

}
