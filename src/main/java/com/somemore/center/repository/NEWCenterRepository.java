package com.somemore.center.repository;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.record.CenterProfileDto;

import java.util.Optional;
import java.util.UUID;

public interface NEWCenterRepository {

    NEWCenter save(NEWCenter center);

    Optional<NEWCenter> findById(UUID id);

    Optional<NEWCenter> findByUserId(UUID userId);

    Optional<CenterProfileDto> findCenterProfileById(UUID centerId);
}
