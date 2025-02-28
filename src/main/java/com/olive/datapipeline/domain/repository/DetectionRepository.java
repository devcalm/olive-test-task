package com.olive.datapipeline.domain.repository;

import com.olive.datapipeline.domain.model.Detection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DetectionRepository extends MongoRepository<Detection, ObjectId> {
}
