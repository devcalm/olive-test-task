package com.olive.datapipeline.web.api;

import com.olive.datapipeline.service.kafka.DetectionProcessEvent;
import com.olive.datapipeline.web.dto.DetectionSaveRequest;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.olive.datapipeline.ConstantsHolder.DETECTION_SAVE_TOPIC;
import static com.olive.datapipeline.ConstantsHolder.DETECTION_BASE_PATH;

@Tag(name = "Detection")
@RestController
@RequiredArgsConstructor
@RequestMapping(DETECTION_BASE_PATH)
public class DetectionController {

    private final KafkaTemplate<String, DetectionProcessEvent> kafkaTemplate;

    @Timed
    @PostMapping("batch")
    @RateLimiter(name = "detectionRateLimiter", fallbackMethod = "rateLimitFallback")
    public void batch(@RequestBody List<DetectionSaveRequest> request) {
        kafkaTemplate.send(DETECTION_SAVE_TOPIC, new DetectionProcessEvent(request));
    }

    public String rateLimitFallback(Exception e) {
        return "Too many requests! Please try again later.";
    }
}
