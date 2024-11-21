package com.somemore;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@EnableJpaAuditing
@SpringBootTest
public abstract class IntegrationTestSupport {

}
