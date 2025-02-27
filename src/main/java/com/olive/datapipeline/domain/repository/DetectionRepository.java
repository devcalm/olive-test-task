package com.olive.datapipeline.domain.repository;

import com.olive.datapipeline.domain.model.Detection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface DetectionRepository extends MongoRepository<Detection, ObjectId> {

    Optional<Detection> findByGuid(UUID guid);
}
