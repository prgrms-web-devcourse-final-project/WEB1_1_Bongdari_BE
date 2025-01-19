package com.somemore.support;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class TestContainerSupport {

    private static final String REDIS_IMAGE = "redis:latest";
    private static final int REDIS_PORT = 6379;
    private static final String MYSQL_IMAGE = "mysql:8";

    private static final GenericContainer REDIS;
    private static final JdbcDatabaseContainer MYSQL;

    static {
        REDIS = new GenericContainer(DockerImageName.parse(REDIS_IMAGE))
                .withExposedPorts(REDIS_PORT)
                .withReuse(true);
        MYSQL = new MySQLContainer(MYSQL_IMAGE);

        REDIS.start();
        MYSQL.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry){
//        registry.add("spring.data.redis.host", REDIS::getHost);
//        registry.add("spring.data.redis.port", () -> String.valueOf(REDIS.getMappedPort(REDIS_PORT)));

        registry.add("spring.data.redis.host", () -> "localhost");
        registry.add("spring.data.redis.port", () -> "6379");
    }
    @DynamicPropertySource
    public static void overrideProps2(DynamicPropertyRegistry registry){
//        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
//        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
//        registry.add("spring.datasource.~username", MYSQL::getUsername);
//        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.datasource.url", () -> "jdbc:mysql://localhost:3306/test");
        registry.add("spring.datasource.username", () -> "root");
        registry.add("spring.datasource.password", () -> "root");
    }
}
