package com.ratingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ratingservice.config.ActionType;
import com.ratingservice.dto.StatMessage;
import com.ratingservice.dto.UserRatingResponse;
import com.ratingservice.jwt.JwtAuthentication;
import com.ratingservice.mapper.RatingMapper;
import com.ratingservice.producer.StatsProducer;
import com.ratingservice.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

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

    public RatingController(RatingService ratingService, RatingMapper mapper,
                            StatsProducer statsProducer) {
        this.ratingService = ratingService;
        this.mapper = mapper;
        this.producer = statsProducer;
    }

    /**
     * Получение рейтинга пользователя по его имени
     * @return сущность с рейтингом пользователя и статус 200 OK
     * @throws Exception при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить рейтинг пользователя")
    @GetMapping()
    public ResponseEntity<UserRatingResponse> getRatingByUsername() throws Exception {
        LocalDateTime startDate = LocalDateTime.now();

        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        int rating = ratingService.getRatingByUsername(username);

        ResponseEntity<UserRatingResponse> res = ResponseEntity.status(HttpStatus.OK).body(mapper.toRatingResponse(rating));

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.RATING, null));
        } catch (JsonProcessingException e) {
            log.error("[RATING] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Изменение рейтинга пользователя
     * @param delta численное изменение рейтинга (на сколько изменился)
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Обновить рейтинг пользователя")
    @PutMapping("/")
    public ResponseEntity<String> updateRating(@RequestParam int delta) throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();

        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        ratingService.updateRating(username, delta);
        ResponseEntity<String> res = ResponseEntity.status(HttpStatus.OK).build();

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.UPDATE_RATING,
                            new HashMap<String, Integer>() {{
                                put("delta", delta);
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[RATING] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

//    /**
//     * Добавление нового пользователя
//     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
//     */
//    @Operation(summary = "Добавить нового пользователя")
//    @PostMapping()
//    public ResponseEntity<String> addUser() throws SQLException {
//        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getUsername();
//        ratingService.addUser(username);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
}
