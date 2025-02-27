package com.olive.datapipeline.service.kafka;

import com.olive.datapipeline.web.dto.DetectionSaveRequest;

import java.util.List;

public record DetectionProcessEvent(
        List<DetectionSaveRequest> detections
) {
}
