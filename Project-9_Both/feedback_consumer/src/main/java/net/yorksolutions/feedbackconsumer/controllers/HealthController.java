package net.yorksolutions.feedbackconsumer.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "UP");
        body.put("service", "feedback-analytics-consumer");
        body.put("timestamp", OffsetDateTime.now().toString());

        return ResponseEntity.ok(body);
    }
}

// curl -i http://localhost:8085/health   : run this command to check health