package com.olive.datapipeline.service.converter;

import com.olive.datapipeline.domain.model.Detection;
import com.olive.datapipeline.web.dto.DetectionSaveRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DetectionDtoConverter {

    public static Detection toEntity(DetectionSaveRequest request) {
        return Detection.builder()
                .guid(request.guid())
                .location(request.location())
                .source(request.source())
                .type(request.type())
                .confidence(request.confidence())
                .date(request.date())
                .build();
    }
}
