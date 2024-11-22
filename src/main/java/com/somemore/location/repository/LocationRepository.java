package com.somemore.location.repository;

import com.somemore.location.domain.Location;
import java.util.Optional;

public interface LocationRepository {

    Location save(Location location);

    Optional<Location> findById(Long id);

    void deleteAllInBatch();
}
