package com.somemore.domains.center.repository.center;

import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.repository.mapper.CenterOverviewInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CenterRepository {
    Center save(Center center);
    boolean existsById(UUID id);
    default boolean doesNotExistById(UUID id) {
        return !existsById(id);
    }
    Optional<Center> findCenterById(UUID id);
    List<CenterOverviewInfo> findCenterOverviewsByIds(List<UUID> ids);
    void deleteAllInBatch();
}
