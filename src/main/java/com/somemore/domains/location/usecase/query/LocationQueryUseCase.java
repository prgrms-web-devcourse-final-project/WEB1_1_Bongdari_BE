package com.somemore.domains.location.usecase.query;

import com.somemore.domains.location.domain.Location;

public interface LocationQueryUseCase {

    Location getById(Long id);

}
