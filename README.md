# Backend Developer Assignment: Real-Time Object Recognition Data Pipeline

This project is a Spring Boot Java application for accepting detection data


### API Endpoints

1. **Detection** <code>/api/detection/batch</code>
     * Save batch detection data **POST** response status **200**
   
## Technologies Used

* Spring WebMVC: Backend framework
* Spring Data Mongo: Database access
* Kafka: Consume data
* Testcontainers: MongoDB/Kafka test processing
* Lombok: Simplified model creation
* Checkstyle: Checking code style
* Jacoco: Test coverage
* Resilience4j: Limit amount of request
* k6: Perform stress testing

## Work Task Explanation

### Database Choice: MongoDB
I chose MongoDB instead of a traditional SQL database due to its ability to handle high-throughput workloads efficiently.
MongoDB’s schema-less nature provides flexibility, and its built-in support for sharding makes it easy to scale horizontally as data volume grows.
This ensures the system remains performant under heavy loads.

### Rate Limiting for Stability
To prevent the system from being overwhelmed by excessive requests,
I implemented a rate-limiting mechanism. This helps maintain stability and ensures fair resource allocation, protecting critical services from potential spikes in traffic.

### Virtual Threads for Memory Optimization
To optimize memory usage and improve concurrency handling, I enabled virtual threads in Spring Boot:
````
spring:
  threads:
    virtual:
      enabled: true
````

Virtual threads (introduced in Java 21) allow for a lightweight concurrency model, significantly reducing the memory footprint of thread management.
This is particularly useful for handling a large number of concurrent tasks without the overhead of traditional OS threads.

### Kafka Producer and Batching Configuration
Since the application relies on Apache Kafka for event-driven processing, I fine-tuned the producer settings to enhance performance
* <b>Batching</b> (batch.size, linger.ms): Increases efficiency by grouping messages before sending, reducing network overhead.
* <b>Compression</b> (compression.type): Reduces payload size, improving Kafka's performance.
* <b>Buffering</b> (buffer.memory): Allocates sufficient memory for storing messages before transmission.
* <b>Retries & Acknowledgments</b> (retries, acks): Ensures message reliability while maintaining low latency.

### Testing

To run application you should have Java 21+ and Docker(docker-compose)
execute command `docker compose up -d --build` 