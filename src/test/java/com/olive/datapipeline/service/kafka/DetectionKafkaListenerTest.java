package com.olive.datapipeline.service.kafka;

import com.olive.datapipeline.BaseTestcontainers;
import com.olive.datapipeline.domain.model.Detection;
import com.olive.datapipeline.domain.repository.DetectionRepository;
import com.olive.datapipeline.web.dto.DetectionSaveRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

import static com.olive.datapipeline.ConstantsHolder.DETECTION_DLQ_TOPIC;
import static com.olive.datapipeline.ConstantsHolder.DETECTION_SAVE_TOPIC;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.instancio.Select.field;

@SpringBootTest
class DetectionKafkaListenerTest extends BaseTestcontainers {

    @Autowired
    private KafkaTemplate<String, DetectionProcessEvent> kafkaTemplate;
    @Autowired
    private DetectionRepository repository;
    @Autowired
    DefaultKafkaConsumerFactory<String, DetectionProcessEvent> defaultKafkaConsumerFactory;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldProcessDetections() {
        var request = Instancio.ofList(DetectionSaveRequest.class).create();

        kafkaTemplate.send(DETECTION_SAVE_TOPIC, new DetectionProcessEvent(request));

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(repository.count()).isEqualTo(request.size()));
    }

    @Test
    void shouldSetEventToDlqTopic() {
        var savedEntity = repository.save(Instancio.of(Detection.class)
                .ignore(field(Detection::getId))
                .create());

        var request = new DetectionProcessEvent(Instancio.ofList(DetectionSaveRequest.class)
                .set(field(DetectionSaveRequest::guid), savedEntity.getGuid())
                .create());

        kafkaTemplate.send(DETECTION_SAVE_TOPIC, request);

        kafkaTemplate.setConsumerFactory(defaultKafkaConsumerFactory);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var consumerRecord = kafkaTemplate.receive(DETECTION_DLQ_TOPIC, 0, 0);
                    assertThat(consumerRecord).isNotNull();
                    assertThat(consumerRecord.value()).isEqualTo(request);
                });
    }
}