package com.olive.datapipeline.domain.model;

import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = Detection.COLLECTION_NAME)
public class Detection {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @Field(targetType = FieldType.STRING)
    private UUID guid;
    private Location location;
    private DetectionType type;
    private String source;
    private double confidence;
    private Instant date;

    public static final String COLLECTION_NAME = "detections";
}
