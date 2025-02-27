package com.olive.datapipeline.web.dto;

import com.olive.datapipeline.domain.model.DetectionType;
import com.olive.datapipeline.domain.model.Location;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(name = "DetectionSaveRequest", description = "Request DTO for storing an detection")
public record DetectionSaveRequest(
        @Schema(description = "A unique identifier for the detected object.",
                example = "37308bce-22c1-4cdb-8832-d557b00b03d6",
                requiredMode = REQUIRED)
        UUID guid,

        @Schema(description = "The latitude and longitude of the detected object",
                example = """
                        {
                            "latitude": 37.7749,
                            "longitude": -122.4194
                        }
                        """,
                requiredMode = REQUIRED)
        Location location,

        @Schema(description = "The category or type of the detected object.",
                example = "MOTORBIKE",
                requiredMode = REQUIRED)
        DetectionType type,

        @Schema(description = "The device identifier.",
                example = "device-123",
                requiredMode = REQUIRED)
        String source,

        @Schema(description = "A confidence score indicating detection reliability.",
                example = "0.98",
                requiredMode = REQUIRED)
        double confidence,

        @Schema(description = "The time of detection.",
                example = "2025-02-25T12:34:56.789Z",
                requiredMode = REQUIRED)
        Instant date
) {
}
