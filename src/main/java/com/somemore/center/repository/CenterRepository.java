package com.somemore.center.repository;

import com.somemore.center.domain.Center;
import com.somemore.center.dto.response.CenterOverviewInfoResponseDto;
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
    List<CenterOverviewInfoResponseDto> findCenterOverviewsByIds(List<UUID> ids);
    void deleteAllInBatch();
}
