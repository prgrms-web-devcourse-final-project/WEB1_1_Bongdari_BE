package com.somemore.global.redis.config;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.global.common.event.ServerEventType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setPassword(password);

        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new CustomJacksonRedisSerializer<>(Object.class));


        template.setValueSerializer(new CustomJacksonRedisSerializer<>(VolunteerRankingResponseDto.class));
        template.setHashValueSerializer(new CustomJacksonRedisSerializer<>(VolunteerRankingResponseDto.class));

        return template;
    }

    @Bean
    public ChannelTopic notificationTopic() {
        return new ChannelTopic("notification");
    }

    @Bean
    public ChannelTopic domainEventTopic() {
        return new ChannelTopic("domainEvent");
    }


    @Bean
    public Map<ServerEventType, ChannelTopic> eventTopicMap(ChannelTopic notificationTopic,
                                                            ChannelTopic domainEventTopic) {
        return Map.of(
                ServerEventType.NOTIFICATION, notificationTopic,
                ServerEventType.DOMAIN_EVENT, domainEventTopic
        );
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        password = StringUtils.isBlank(password) ? null : password;

        config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + host + ":" + port)
                .setPassword(password);

        return Redisson.create(config);
    }
}
