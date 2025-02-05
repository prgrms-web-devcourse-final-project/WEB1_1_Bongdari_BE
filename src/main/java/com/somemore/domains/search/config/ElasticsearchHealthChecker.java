package com.somemore.domains.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.HealthStatus;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@Component
@RequiredArgsConstructor
public class ElasticsearchHealthChecker {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchHealthChecker.class);
    private final ElasticsearchClient elasticsearchClient;

    public boolean isElasticsearchRunning() {
        try {
            HealthResponse healthResponse = elasticsearchClient.cluster().health();
            return healthResponse.status() != HealthStatus.Red;
        } catch (RuntimeException | IOException e) {
            logger.info("Elasticsearch is not available: {}", e.getMessage());
            return false;
        }
    }
}
