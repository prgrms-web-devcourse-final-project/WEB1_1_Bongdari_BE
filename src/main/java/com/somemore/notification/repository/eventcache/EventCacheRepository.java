package com.somemore.notification.repository.eventcache;

import java.util.Map;
import java.util.UUID;

public interface EventCacheRepository {
    void save(String eventCacheId, Object event);

    Map<String, Object> findAllByUserId(UUID userId);

    void deleteAllByUserId(UUID userId);
}
