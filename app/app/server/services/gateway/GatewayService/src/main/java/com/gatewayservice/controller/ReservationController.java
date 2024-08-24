package com.gatewayservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gatewayservice.config.ActionType;
import com.gatewayservice.dto.*;
import com.gatewayservice.jwt.JwtAuthentication;
import com.gatewayservice.producer.StatsProducer;
import com.gatewayservice.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "RESERVATIONS")
@RequestMapping("/reservations")
public class ReservationController {
    /**
     * Сервис, работающий с прокатом книг
     */
    private final ReservationService reservationService;

    private final StatsProducer producer;

    public ReservationController(ReservationService reservationService, StatsProducer producer) {
        this.reservationService = reservationService;
        this.producer = producer;
    }

    /**
     * Получение списка книг, взятых в прокат по имени пользователя
     * @return список книг, взятых в прокат
     */
    @Operation(summary = "Получение списка книг, взятых пользователем в прокат")
    @GetMapping()
    public ResponseEntity<ArrayList<BookReservationResponse>> getReservations() {
        LocalDateTime startDate = LocalDateTime.now();

        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        String token = auth.getToken();
        ResponseEntity<ArrayList<BookReservationResponse>> res = reservationService.getAllReservations(token);

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.RESERVATIONS_BY_USER, null));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Взять книгу в библиотеке
     * @param req информация о запросе
     * @return список книг, взятых в прокат
     */
    @Operation(summary = "Взять книгу в библиотеке")
    @PostMapping()
    public ResponseEntity<?> takeBook(@RequestBody TakeBookRequest req) {
        LocalDateTime startDate = LocalDateTime.now();

        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        String token = auth.getToken();
        TakeBookResponse reservation = reservationService.takeBook(token, req);
        ResponseEntity res = null;

        if (reservation == null)
            res = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse("Service unavailable"));
        else
            res = ResponseEntity.status(HttpStatus.OK).body(reservation);

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.CREATE_RESERVATION, req));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Вернуть книгу в библиотеку
     * @param reservationUid UUID брони, которую закрывает читатель
     * @param req информация о возврате
     */
    @Operation(summary = "Вернуть книгу в библиотеку")
    @PostMapping("/{reservationUid}/return")
    public ResponseEntity<String> returnBook(@PathVariable UUID reservationUid,
                                                   @RequestBody ReturnBookRequest req) throws JsonProcessingException {
        LocalDateTime startDate = LocalDateTime.now();

        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        String token = auth.getToken();
        HttpStatus status = reservationService.returnBook(reservationUid, token, req);

        ResponseEntity<String> res = ResponseEntity.status(status).build();

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.CLOSE_RESERVATION,
                            new HashMap<String, UUID>() {{
                                put("reservationUUID", reservationUid);
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }
}
