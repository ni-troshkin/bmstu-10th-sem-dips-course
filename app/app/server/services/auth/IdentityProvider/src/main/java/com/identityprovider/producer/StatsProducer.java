package com.identityprovider.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.identityprovider.dto.StatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StatsProducer {
    @Value("${topic.stats.name}")
    private String statsTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public StatsProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public String sendMessage(StatMessage statMsg) throws JsonProcessingException {
        String msg = objectMapper.writeValueAsString(statMsg);

        kafkaTemplate.send(statsTopic, msg);

        log.info("stat message produced {}", msg);

        return "message sent";
    }
}
