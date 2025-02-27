package com.olive.datapipeline.web.api;

import com.olive.datapipeline.BaseTestcontainers;
import com.olive.datapipeline.web.dto.DetectionSaveRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static com.olive.datapipeline.ConstantsHolder.DETECTION_BASE_PATH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DetectionControllerTest extends BaseTestcontainers {

    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    private String baseUrl;

    @BeforeEach
    void setup() {
        baseUrl = String.format("http://localhost:%d/%s/batch", port, DETECTION_BASE_PATH);
    }

    @Test
    void shouldSaveDetection() {
        var request = Instancio.ofList(DetectionSaveRequest.class).create();

        var response = restTemplate.postForEntity(baseUrl, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
