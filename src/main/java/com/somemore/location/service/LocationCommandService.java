package com.somemore.location.service;

import com.somemore.location.dto.request.LocationCreateRequestDto;

public interface LocationCommandService {

    public Long createLocation(LocationCreateRequestDto dto);

}
