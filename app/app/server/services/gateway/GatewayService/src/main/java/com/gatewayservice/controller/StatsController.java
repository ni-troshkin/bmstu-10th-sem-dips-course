package com.gatewayservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gatewayservice.config.ActionType;
import com.gatewayservice.dto.RequestAvg;
import com.gatewayservice.dto.ServiceAvg;
import com.gatewayservice.dto.StatMessage;
import com.gatewayservice.jwt.JwtAuthentication;
import com.gatewayservice.producer.StatsProducer;
import com.gatewayservice.service.StatsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "STATS")
@RequestMapping("stats")
public class StatsController {
    /**
     * Контроллер, работающий с авторизацией
     */
    private final StatsService statsService;

    private final StatsProducer producer;


    public StatsController(StatsService statsService, StatsProducer producer) {
        this.statsService = statsService;
        this.producer = producer;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping()
    public ResponseEntity<ArrayList<StatMessage>> getAll() throws SQLException {
        log.info("[GATEWAY]: Request to get all activity info was caught.");
        LocalDateTime startDate = LocalDateTime.now();

        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        String token = auth.getToken();

        ResponseEntity<ArrayList<StatMessage>> res = statsService.getAllStats(token);

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.ALL_STATS, null));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/services/avgtime")
    public ResponseEntity<ArrayList<ServiceAvg>> getServiceAvgTime() throws SQLException {
        log.info("[GATEWAY]: Request to get average services' processing time was caught.");
        LocalDateTime startDate = LocalDateTime.now();

        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        String token = auth.getToken();

        ResponseEntity<ArrayList<ServiceAvg>> res = statsService.getServiceStats(token);

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.SERVICE_STATS, null));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/requests/avgtime")
    public ResponseEntity<ArrayList<RequestAvg>> getRequestAvgTime() throws SQLException {
        log.info("[GATEWAY]: Request to get average queries' processing time was caught.");
        LocalDateTime startDate = LocalDateTime.now();

        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        String token = auth.getToken();

        ResponseEntity<ArrayList<RequestAvg>> res = statsService.getRequestStats(token);

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.REQUEST_STATS, null));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }
}
