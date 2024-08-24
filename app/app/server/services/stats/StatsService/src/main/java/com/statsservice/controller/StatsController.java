package com.statsservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.statsservice.config.ActionType;
import com.statsservice.dto.Message;
import com.statsservice.dto.RequestAvg;
import com.statsservice.dto.ServiceAvg;
import com.statsservice.dto.StatMessage;
import com.statsservice.jwt.JwtAuthentication;
import com.statsservice.producer.StatsProducer;
import com.statsservice.service.StatsService;
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
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "STATS")
@RequestMapping("stats")
public class StatsController {
    private StatsService statsService;
    private StatsProducer producer;

    public StatsController(StatsService statsService, StatsProducer producer) {
        this.statsService = statsService;
        this.producer = producer;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping()
    public ResponseEntity<List<Message>> getAll() throws SQLException {
        log.info("[STATS]: Request to get all activity info was caught.");
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();

        ResponseEntity<List<Message>> res = ResponseEntity
                .ok()
                .body(statsService.select());

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.ALL_STATS, null));
        } catch (JsonProcessingException e) {
            log.error("[STATS] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/services/avgtime")
    public ResponseEntity<List<ServiceAvg>> getServiceAvgTime() throws SQLException {
        log.info("[STATS]: Request to get average services' processing time was caught.");
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();

        ResponseEntity<List<ServiceAvg>> res =  ResponseEntity
                .ok()
                .body(statsService.selectServiceAvgTime());

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.SERVICE_STATS, null));
        } catch (JsonProcessingException e) {
            log.error("[STATS] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/requests/avgtime")
    public ResponseEntity<List<RequestAvg>> getRequestAvgTime() throws SQLException {
        log.info("[STATS]: Request to get average queries' processing time was caught.");
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();

        ResponseEntity<List<RequestAvg>> res =  ResponseEntity
                .ok()
                .body(statsService.selectRequestAvgTime());

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.REQUEST_STATS, null));
        } catch (JsonProcessingException e) {
            log.error("[STATS] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }
}
