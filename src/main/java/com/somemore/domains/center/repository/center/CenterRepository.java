package com.somemore.domains.center.repository.center;

import com.somemore.center.repository.record.CenterOverviewInfo;
import com.somemore.domains.center.domain.Center;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CenterRepository {

    Center save(Center center);

    boolean existsById(UUID id);

    default boolean doesNotExistById(UUID id) {
        return !existsById(id);
    }

    Optional<Center> findCenterById(UUID id);

    List<CenterOverviewInfo> findCenterOverviewsByIds(List<UUID> ids);

    void deleteAllInBatch();

    String findNameById(UUID id);
}
