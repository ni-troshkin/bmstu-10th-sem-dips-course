package com.gatewayservice.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gatewayservice.dto.UpdateRatingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RatingProducer {
    @Value("${topic.rating.name}")
    private String ratingTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public RatingProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public String sendMessage(String username, int delta) throws JsonProcessingException {
        UpdateRatingRequest req = new UpdateRatingRequest(username, delta);
        String msg = objectMapper.writeValueAsString(req);

        kafkaTemplate.send(ratingTopic, msg);

        log.info("rating update produced {}", msg);

        return "message sent";
    }
}
