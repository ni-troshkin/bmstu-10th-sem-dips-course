package com.gatewayservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gatewayservice.config.ActionType;
import com.gatewayservice.dto.ErrorResponse;
import com.gatewayservice.dto.StatMessage;
import com.gatewayservice.dto.UserRatingResponse;
import com.gatewayservice.jwt.JwtAuthentication;
import com.gatewayservice.mapper.RatingMapper;
import com.gatewayservice.producer.StatsProducer;
import com.gatewayservice.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "RATING")
@RequestMapping("/rating")
public class RatingController {
    /**
     * Сервис, работающий с пользователями
     */
    private final RatingService ratingService;

    private final RatingMapper mapper;
    private final StatsProducer producer;

    public RatingController(RatingService ratingService, RatingMapper mapper, StatsProducer producer) {
        this.ratingService = ratingService;
        this.mapper = mapper;
        this.producer = producer;
    }

    /**
     * Получение рейтинга пользователя по его имени
     * @return сущность с рейтингом пользователя и статус 200 OK
     * @throws Exception при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить рейтинг пользователя")
    @GetMapping()
    public ResponseEntity<?> getPersonById() throws Exception {
        LocalDateTime startDate = LocalDateTime.now();

        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        String token = auth.getToken();
        ResponseEntity<UserRatingResponse> response = ratingService.getUserRating(token);
        if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse("Service unavailable"));

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.RATING, null));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }

        return response;
    }
}
