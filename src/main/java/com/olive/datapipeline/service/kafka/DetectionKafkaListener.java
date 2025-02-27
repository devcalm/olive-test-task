package com.olive.datapipeline.service.kafka;

import com.olive.datapipeline.domain.repository.DetectionRepository;
import com.olive.datapipeline.service.converter.DetectionDtoConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.olive.datapipeline.ConstantsHolder.DETECTION_DLQ_TOPIC;
import static com.olive.datapipeline.ConstantsHolder.DETECTION_SAVE_TOPIC;
import static com.olive.datapipeline.ConstantsHolder.DETECTION_GROUP_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DetectionKafkaListener {

    private final DetectionRepository repository;
    private final KafkaTemplate<String, DetectionProcessEvent> kafkaTemplate;

    @KafkaListener(topics = DETECTION_SAVE_TOPIC, groupId = DETECTION_GROUP_ID)
    public void processDetections(DetectionProcessEvent event) {
        try {
            repository.saveAll(event.detections().stream().map(DetectionDtoConverter::toEntity).toList());
        } catch (Exception e) {
            log.error("Failed to process detection. Sending to DLQ", e);
            kafkaTemplate.send(DETECTION_DLQ_TOPIC, event);
        }
    }

    @KafkaListener(topics = DETECTION_DLQ_TOPIC, groupId = DETECTION_GROUP_ID)
    public void processDlqEvents(DetectionProcessEvent event) {
        log.warn("DLQ received message: {}", event);
        //ToDo we should implement logic here
    }
}
