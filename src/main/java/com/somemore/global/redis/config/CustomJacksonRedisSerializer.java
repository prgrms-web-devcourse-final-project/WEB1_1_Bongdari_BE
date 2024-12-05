package com.somemore.global.redis.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

public class CustomJacksonRedisSerializer<T> implements RedisSerializer<T> {

    private final ObjectMapper objectMapper;

    public CustomJacksonRedisSerializer(Class<T> type) {
        this.objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule()) // Java 8 Time 모듈 등록
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        objectMapper.registerSubtypes(type);
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Could not serialize object", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        try {
            return objectMapper.readValue(bytes, objectMapper.constructType(Object.class));
        } catch (IOException e) {
            throw new SerializationException("Could not deserialize bytes", e);
        }
    }
}
