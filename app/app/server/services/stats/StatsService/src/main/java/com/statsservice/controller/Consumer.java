package com.statsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.statsservice.dto.Message;
import com.statsservice.service.StatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {
    private static final String statsTopic = "${topic.name}";

    private final ObjectMapper objectMapper;
    private final StatsService statsService;

    @Autowired
    public Consumer(ObjectMapper objectMapper, StatsService statsService) {
        this.objectMapper = objectMapper;
        this.statsService = statsService;
    }

    @KafkaListener(topics = statsTopic)
    public ResponseEntity<String> consumeReturnBook(String message) throws Exception {
        log.info("[STATS] message consumed {}", message);
        Message msg = objectMapper.readValue(message, Message.class);

        try {
            statsService.process(msg);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            log.error("[STATS]: {} was caught error, err={}", message, e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
