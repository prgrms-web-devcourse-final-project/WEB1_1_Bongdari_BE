package com.somemore.notification.repository.eventcache;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EventCacheRepositoryImpl implements EventCacheRepository {
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public void save(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, Object> findAllByUserId(UUID userId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId.toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        eventCache.keySet().stream()
                .filter(key -> key.startsWith(userId.toString()))
                .forEach(eventCache::remove);
    }
}