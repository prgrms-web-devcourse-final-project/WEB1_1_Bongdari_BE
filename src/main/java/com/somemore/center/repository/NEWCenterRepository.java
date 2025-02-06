package com.somemore.center.repository;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.record.CenterOverviewInfo;
import com.somemore.center.repository.record.CenterProfileDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NEWCenterRepository {

    NEWCenter save(NEWCenter center);

    Optional<NEWCenter> findById(UUID id);

    Optional<NEWCenter> findByUserId(UUID userId);

    Optional<CenterProfileDto> findCenterProfileById(UUID centerId);

    boolean existsById(UUID id);

    default boolean doesNotExistById(UUID id) {
        return !existsById(id);
    }

    List<CenterOverviewInfo> findOverviewInfosByIds(List<UUID> ids);

    String findNameById(UUID id);

}
