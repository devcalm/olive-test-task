package com.olive.datapipeline;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class BaseTestcontainers {

    private static final MongoDBContainer mongoDbContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:8.0"));
    private static final ConfluentKafkaContainer kafkaContainer =
            new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.7.1"));

    static {
        mongoDbContainer.start();
        kafkaContainer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(mongoDbContainer::stop));
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaContainer::stop));
    }

    @DynamicPropertySource
    static void configureTestcontainers(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl);
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }
}
