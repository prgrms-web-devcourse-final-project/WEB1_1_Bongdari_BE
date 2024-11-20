package com.somemore.location.usecase;

import com.somemore.location.dto.request.LocationCreateRequestDto;

public interface CreateLocationUseCase {

    Long createLocation(LocationCreateRequestDto requestDto);

}
