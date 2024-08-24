package com.gatewayservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gatewayservice.config.ActionType;
import com.gatewayservice.dto.*;
import com.gatewayservice.jwt.JwtAuthentication;
import com.gatewayservice.producer.StatsProducer;
import com.gatewayservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
@Tag(name = "AUTH")
@RequestMapping("oauth")
public class AuthController {
    /**
     * Контроллер, работающий с авторизацией
     */
    private final AuthService authService;

    private final StatsProducer producer;


    public AuthController(AuthService authService, StatsProducer producer) {
        this.authService = authService;
        this.producer = producer;
    }

    /**
     * Запрос на авторизацию
     * @param client_id идентификатор клиента
     * @return редирект на страницу авторизации
     */
    @Operation(summary = "Запрос на авторизацию")
    @PostMapping("/authorize")
    public ResponseEntity<String> authorize(@RequestParam String client_id) {
        LocalDateTime startDate = LocalDateTime.now();
        ResponseEntity<String> res = authService.authorize(client_id);
        try {
            producer.sendMessage(
                    new StatMessage(
                            null, startDate, LocalDateTime.now(), ActionType.AUTHORIZATION,
                            new HashMap<String, String>() {{
                                put("client_id", client_id);
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }
        return res;
    }

    /**
     * Запрос токена
     * @param req информация о клиенте
     * @return токены доступа клиента
     */
    @Operation(summary = "Запрос токена")
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> token(@RequestBody TokenRequest req) {
        LocalDateTime startDate = LocalDateTime.now();
        ResponseEntity<TokenResponse> res = authService.token(req);
        try {
            producer.sendMessage(
                    new StatMessage(
                            null, startDate, LocalDateTime.now(), ActionType.TOKEN, req));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }
        return res;
    }

    /**
     * Выход из аккаунта
     * @return 200
     */
    @Operation(summary = "Выход")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();

        ResponseEntity<String> res = authService.logout(username);
        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.LOGOUT, null));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }
        return res;
    }

    /**
     * Добавление нового пользователя
     * @return 201
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Создание нового пользователя")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto req) {
        LocalDateTime startDate = LocalDateTime.now();

        ResponseEntity<String> res = authService.register(req);
        try {
            producer.sendMessage(
                    new StatMessage(
                            req.getProfile().username, startDate, LocalDateTime.now(), ActionType.REGISTRATION, req));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }
        return res;
    }
}
