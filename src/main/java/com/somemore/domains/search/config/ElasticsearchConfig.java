package com.somemore.domains.search.config;

import com.somemore.domains.search.annotation.ConditionalOnElasticSearchEnabled;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
@ConditionalOnElasticSearchEnabled
public class ElasticsearchConfig extends ElasticsearchConfiguration {
    @Value("${elastic.search.uri}")
    private String uri;
    @Value("${elastic.search.username}")
    private String username;
    @Value("${elastic.search.password}")
    private String password;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(uri)
                .withBasicAuth(username, password)
                .build();
    }
}
