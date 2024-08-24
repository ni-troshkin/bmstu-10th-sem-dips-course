package com.gatewayservice.service;

import com.gatewayservice.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class StatsService {
    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingService.class);
    private final CircuitBreaker allStatsCircuitBreaker;
    private final CircuitBreaker requestStatsCircuitBreaker;
    private final CircuitBreaker serviceStatsCircuitBreaker;
    private final String serverUrl;

    public StatsService(RestTemplate restTemplate, CircuitBreakerFactory cbf,
                         @Value("${stats.server.url}") String serverUrl
    ) {
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = cbf;
        this.serverUrl = serverUrl;
        this.allStatsCircuitBreaker = circuitBreakerFactory.create("allStatsCb");
        this.requestStatsCircuitBreaker = circuitBreakerFactory.create("requestStatsCb");
        this.serviceStatsCircuitBreaker = circuitBreakerFactory.create("serviceStatsCb");
    }

    public ResponseEntity<ArrayList<StatMessage>> fallbackAllStatsResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<ArrayList<ServiceAvg>> fallbackServiceStatsResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<ArrayList<RequestAvg>> fallbackRequestStatsResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<ArrayList<StatMessage>> getAllStats(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<ArrayList<StatMessage>> stats = null;
        try {
            stats = allStatsCircuitBreaker.run(
                    () -> restTemplate.exchange(
                            serverUrl + "/api/v1/stats",
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<ArrayList<StatMessage>>() {
                            }
                    ), throwable -> fallbackAllStatsResponse()
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return stats;
    }

    public ResponseEntity<ArrayList<ServiceAvg>> getServiceStats(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<ArrayList<ServiceAvg>> stats = null;
        try {
            stats = serviceStatsCircuitBreaker.run(
                    () -> restTemplate.exchange(
                            serverUrl + "/api/v1/stats/services/avgtime",
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<ArrayList<ServiceAvg>>() {
                            }
                    ), throwable -> fallbackServiceStatsResponse()
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return stats;
    }

    public ResponseEntity<ArrayList<RequestAvg>> getRequestStats(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<ArrayList<RequestAvg>> stats = null;
        try {
            stats = requestStatsCircuitBreaker.run(
                    () -> restTemplate.exchange(
                            serverUrl + "/api/v1/stats/requests/avgtime",
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<ArrayList<RequestAvg>>() {
                            }
                    ), throwable -> fallbackRequestStatsResponse()
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return stats;
    }

}
