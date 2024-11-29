package com.somemore.location.usecase.query;

import com.somemore.location.domain.Location;

public interface LocationQueryUseCase {

    Location getById(Long id);

}
