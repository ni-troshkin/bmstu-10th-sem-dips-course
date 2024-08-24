package com.ratingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratingservice.config.ActionType;
import com.ratingservice.dto.StatMessage;
import com.ratingservice.dto.UpdateRatingRequest;
import com.ratingservice.dto.UserRatingResponse;
import com.ratingservice.mapper.RatingMapper;
import com.ratingservice.producer.StatsProducer;
import com.ratingservice.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Component
public class Consumer {
    private static final String ratingTopic = "${topic.name}";

    private final ObjectMapper objectMapper;
    private final RatingService ratingService;

    private final RatingMapper mapper;
    private final StatsProducer producer;

    @Autowired
    public Consumer(ObjectMapper objectMapper, RatingService ratingService,
                    RatingMapper mapper, StatsProducer producer) {
        this.objectMapper = objectMapper;
        this.ratingService = ratingService;
        this.mapper = mapper;
        this.producer = producer;
    }

    @KafkaListener(topics = ratingTopic)
    public ResponseEntity<String> consumeRatingUpdate(String message) throws SQLException, JsonProcessingException {
        log.info("message consumed {}", message);
        LocalDateTime startDate = LocalDateTime.now();
        UpdateRatingRequest req = objectMapper.readValue(message, UpdateRatingRequest.class);

        ratingService.updateRating(req.getUsername(), req.getDelta());
        ResponseEntity<String> res = ResponseEntity.status(HttpStatus.OK).build();

        try {
            producer.sendMessage(
                    new StatMessage(
                            req.getUsername(), startDate, LocalDateTime.now(), ActionType.UPDATE_RATING, req));
        } catch (JsonProcessingException e) {
            log.error("[RATING] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }
}
