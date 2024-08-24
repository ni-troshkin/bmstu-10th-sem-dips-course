package com.reservationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reservationservice.config.ActionType;
import com.reservationservice.dto.ReservationRequest;
import com.reservationservice.dto.ReservationResponse;
import com.reservationservice.dto.StatMessage;
import com.reservationservice.entity.Reservation;
import com.reservationservice.jwt.JwtAuthentication;
import com.reservationservice.mapper.ReservationMapper;
import com.reservationservice.producer.StatsProducer;
import com.reservationservice.service.ReservationService;
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
import java.sql.SQLException;
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
     * Сервис, работающий с пользователями
     */
    private final ReservationService reservationService;

    private final ReservationMapper mapper;
    private final StatsProducer producer;

    public ReservationController(ReservationService reservationService, ReservationMapper mapper,
                                 StatsProducer producer) {
        this.reservationService = reservationService;
        this.mapper = mapper;
        this.producer = producer;
    }

    /**
     * Получение списка книг, взятых в прокат по имени пользователя
     * @return список книг, взятых в прокат
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получение списка книг, взятых пользователем в прокат")
    @GetMapping()
    public ResponseEntity<ArrayList<ReservationResponse>> getReservations() throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        ArrayList<Reservation> reservations = reservationService.getAllReservations(username);

        ArrayList<ReservationResponse> allRes = new ArrayList<>();
        for (Reservation res : reservations) {
            allRes.add(mapper.toReservationResponse(res));
        }

        ResponseEntity<ArrayList<ReservationResponse>> res = ResponseEntity.status(HttpStatus.OK).body(allRes);

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.RESERVATIONS_BY_USER, null));
        } catch (JsonProcessingException e) {
            log.error("[RESERVATIONS] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Получение количества книг на руках у пользователя
     * @return количество книг, взятых в прокат
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получение количества книг на руках у пользователя")
    @GetMapping("/rented")
    public ResponseEntity<Integer> countReservations() throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        int reservationsCnt = reservationService.countRented(username);
        ResponseEntity<Integer> res = ResponseEntity.status(HttpStatus.OK).body(Integer.valueOf(reservationsCnt));

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.COUNT_RESERVATIONS, null));
        } catch (JsonProcessingException e) {
            log.error("[RESERVATIONS] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Создание новой брони
     * @param reqReservation объект с информацией, которую клиент отправил при создании пользователя
     * @return информация о новой брони книги
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     * @throws URISyntaxException при неуспешном создании URI нового пользователя
     */
    @Operation(summary = "Взять книгу в библиотеке")
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
                                                    @RequestBody ReservationRequest reqReservation) throws URISyntaxException, SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        Reservation reservation = mapper.fromReservationRequest(reqReservation, username);
        reservationService.createReservation(reservation);

        ResponseEntity<ReservationResponse> res = ResponseEntity.status(HttpStatus.OK).body(mapper.toReservationResponse(reservation));

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.CREATE_RESERVATION, reqReservation));
        } catch (JsonProcessingException e) {
            log.error("[RESERVATIONS] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Вернуть книгу в библиотеку
     * @param reservationUid UUID брони, которую закрывает читатель
     * @param isExpired просрочена книга или нет
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Вернуть книгу в библиотеку")
    @PostMapping("/{reservationUid}/return")
    public ResponseEntity<String> closeReservation(@PathVariable UUID reservationUid,
                                                                @RequestParam boolean isExpired) throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        reservationService.closeReservation(reservationUid, isExpired);
        ResponseEntity<String> res = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.CLOSE_RESERVATION,
                            new HashMap<String, String>() {{
                                put("reservation_uuid", reservationUid.toString());
                                put("is_expired", String.valueOf(isExpired));
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[RESERVATIONS] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Получить информацию о брони
     * @param reservationUid UUID брони, о которой нужна информация
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить информацию о брони")
    @GetMapping("/{reservationUid}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable UUID reservationUid) throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        Reservation reservation = reservationService.getReservation(reservationUid);
        ResponseEntity<ReservationResponse> res = ResponseEntity.status(HttpStatus.OK).body(mapper.toReservationResponse(reservation));

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.RESERVATION,
                            new HashMap<String, UUID>() {{
                                put("reservation_uuid", reservationUid);
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[RESERVATIONS] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Отменить бронь
     * @param reservationUid UUID брони, которую нужно удалить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Удалить бронь")
    @DeleteMapping("/{reservationUid}")
    public ResponseEntity<String> deleteReservation(@PathVariable UUID reservationUid) throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        reservationService.deleteReservation(reservationUid);
        ResponseEntity<String> res = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.DELETE_RESERVATION,
                            new HashMap<String, UUID>() {{
                                put("reservation_uuid", reservationUid);
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[RESERVATIONS] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }
}
