package com.somemore.support;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-mysql")
@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTestSupport extends TestContainerSupport{

}
