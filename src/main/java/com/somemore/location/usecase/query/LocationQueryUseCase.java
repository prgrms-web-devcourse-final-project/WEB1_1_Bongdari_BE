package com.somemore.location.usecase.query;

import com.somemore.location.domain.Location;
import java.util.Optional;

public interface LocationQueryUseCase {

    Optional<Location> findById(Long id);

}
