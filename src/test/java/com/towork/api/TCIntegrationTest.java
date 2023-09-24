package com.towork.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
@SpringBootTest
public abstract class TCIntegrationTest {

    public static final String TEST_MYSQL_CONTAINER_NAME = "test_mysql_1";
    public static final String TEST_REDIS_CONTAINER_NAME = "test_redis_1";

    public static final String TEST_DOCKER_COMPOSE_PATH = "src/test/resources/docker-compose.test.yml";

    public static final DockerComposeContainer dockerComposeContainer;

    static {
        dockerComposeContainer = new DockerComposeContainer(new File(TEST_DOCKER_COMPOSE_PATH))
                .waitingFor(TEST_MYSQL_CONTAINER_NAME, Wait.forHealthcheck())
                .waitingFor(TEST_REDIS_CONTAINER_NAME, Wait.forHealthcheck());
        dockerComposeContainer.start();
    }

}
