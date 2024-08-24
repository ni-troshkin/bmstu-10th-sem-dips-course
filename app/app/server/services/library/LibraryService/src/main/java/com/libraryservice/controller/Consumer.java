package com.libraryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryservice.config.ActionType;
import com.libraryservice.dto.ReturnBookRequest;
import com.libraryservice.dto.StatMessage;
import com.libraryservice.exception.BookIsNotAvailable;
import com.libraryservice.producer.StatsProducer;
import com.libraryservice.service.LibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Component
public class Consumer {
    private static final String libraryTopic = "${topic.name}";

    private final ObjectMapper objectMapper;
    private final LibraryService libraryService;

    private final StatsProducer producer;

//    private final RatingMapper mapper;

    @Autowired
    public Consumer(ObjectMapper objectMapper, LibraryService libraryService,
                    StatsProducer producer) {
        this.objectMapper = objectMapper;
        this.libraryService = libraryService;
        this.producer = producer;
    }

    @KafkaListener(topics = libraryTopic)
    public ResponseEntity<String> consumeReturnBook(String message) throws Exception {
        log.info("message consumed {}", message);
        ReturnBookRequest req = objectMapper.readValue(message, ReturnBookRequest.class);
        LocalDateTime startDate = LocalDateTime.now();

        libraryService.returnBook(UUID.fromString(req.getLibraryUid()),
                                  UUID.fromString(req.getBookUid()));

        ResponseEntity<String> res = ResponseEntity.status(HttpStatus.OK).build();

        try {
            producer.sendMessage(
                    new StatMessage(
                            null, startDate, LocalDateTime.now(), ActionType.BOOK_OPERATION, req));
        } catch (JsonProcessingException e) {
            log.error("[LIBRARY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }
}
