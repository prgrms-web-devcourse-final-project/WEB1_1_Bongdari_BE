package com.somemore.sse.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public Map<String, SseEmitter> findAllByReceiverId(UUID receiverId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(receiverId.toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    @Override
    public void deleteAllByReceiverId(UUID receiverId) {
        emitters.keySet().stream()
                .filter(key -> key.startsWith(receiverId.toString()))
                .forEach(emitters::remove);
    }
}