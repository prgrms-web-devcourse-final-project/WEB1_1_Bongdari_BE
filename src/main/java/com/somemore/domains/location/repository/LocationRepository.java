package com.somemore.domains.location.repository;

import com.somemore.domains.location.domain.Location;

import java.util.Optional;

public interface LocationRepository {

    Location save(Location location);

    Location saveAndFlush(Location location);

    Optional<Location> findById(Long id);

    void deleteAllInBatch();
}
