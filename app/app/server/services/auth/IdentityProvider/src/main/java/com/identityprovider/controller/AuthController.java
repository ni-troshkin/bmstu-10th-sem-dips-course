package com.identityprovider.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.identityprovider.config.ActionType;
import com.identityprovider.dto.*;
import com.identityprovider.producer.StatsProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import com.identityprovider.service.AuthService;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("oauth")
public class AuthController {
    private final RestTemplate restTemplate;
    private final StatsProducer producer;

    @Autowired
    private Keycloak keycloak;

    public AuthController(RestTemplate restTemplate, StatsProducer producer) {
        this.restTemplate = restTemplate;
        this.producer = producer;
    }

    @Autowired
    private AuthService service;

    @PostMapping(value = "/auth")
    public ResponseEntity<String> authorize(@RequestBody AuthRequest request) {
        log.info("[AUTH]: authorization request was caught.");
        LocalDateTime startDate = LocalDateTime.now();

        String url = "http://localhost:8081/realms/LibraryIdentityProvider/protocol/openid-connect/auth" +
                "?response_type=code" +
                "&client_id=" + request.getClientId() +
                "&redirect_uri=http%3A%2F%2Flocalhost%3A8090%2Fapi%2Fv1%2Fmanage%2Fhealth" +
                "&scope=openid" +
                "&state=xyz";
        log.info(url);
        ResponseEntity<String> res = ResponseEntity.status(HttpStatus.FOUND).header("Location", url).body("");

        try {
            producer.sendMessage(
                    new StatMessage(
                            null, startDate, LocalDateTime.now(), ActionType.AUTHORIZATION, request));
        } catch (JsonProcessingException e) {
            log.error("[AUTH] error sending kafka msg, {}", e.getMessage());
        }
        return res;
//        return service.getToken(request);
    }

    @PostMapping(value = "/token")
    public ResponseEntity<TokenResponse> token(@RequestBody TokenRequest request) throws JsonProcessingException {
        log.info("[AUTH]: registration request={} was caught.", request);
        LocalDateTime startDate = LocalDateTime.now();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        TokenKeycloakRequest req = new TokenKeycloakRequest(request);
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(
//                req.getMap(),
//                headers
//        );
        String body = "grant_type=authorization_code" +
                "&client_id=" + request.getClientId() +
                "&client_secret=" + request.getClientSecret() +
                "&code=" + request.getAuthenticationCode() +
                "&redirect_uri=http%3A%2F%2Flocalhost%3A8090%2Fapi%2Fv1%2Fmanage%2Fhealth";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<TokenResponse> response = null;
        String keycloakUrl = "http://keycloak_lib:8080/realms/LibraryIdentityProvider/protocol/openid-connect/token";
        try {
            response = restTemplate.exchange(
                    keycloakUrl,
                    HttpMethod.POST,
                    entity,
                    TokenResponse.class
            );
            System.out.println(response.getBody());
//        HttpEntity<String> entity = new HttpEntity<>("body", headers);
//        ResponseEntity<TokenResponse> response = null;
//        String keycloakUrl = "http://localhost:8081/realms/LibraryIdentityProvider/protocol/openid-connect";
//        try {
//            response = restTemplate.exchange(
//                    keycloakUrl + "/token?grant_type=authorization_code" +
//                            "&client_id=" + request.getClientId() +
//                            "&client_secret=" + request.getClientSecret() +
//                            "&code=" + request.getAuthenticationCode() +
//                            "&redirect_uri=http%3A%2F%2Flocalhost%3A8090%2Fmanage%2Fhealth",
//                    HttpMethod.POST,
//                    entity,
//                    TokenResponse.class
//            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        } catch (ResourceAccessException e) {
            response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        try {
            producer.sendMessage(
                    new StatMessage(
                            null, startDate, LocalDateTime.now(), ActionType.TOKEN, request));
        } catch (JsonProcessingException e) {
            log.error("[AUTH] error sending kafka msg, {}", e.getMessage());
        }
        return response;
//        return new ResponseEntity(service.register(request));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> token(@RequestParam String username) throws JsonProcessingException {
        log.info("[AUTH]: logout request={} was caught.", username);
        LocalDateTime startDate = LocalDateTime.now();

        String id = keycloak.realm("LibraryIdentityProvider")
                .users()
                .searchByUsername(username, true)
                .get(0)
                .getId();

        keycloak.realm("LibraryIdentityProvider")
                .users()
                .get(id)
                .logout();
        ResponseEntity<String> res = ResponseEntity.status(HttpStatus.OK).build();

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.LOGOUT, null));
        } catch (JsonProcessingException e) {
            log.error("[AUTH] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto req) throws JsonProcessingException {
        log.info("[AUTH]: register by admin request={} was caught.", req);
        LocalDateTime startDate = LocalDateTime.now();

        UserRepresentation newUser = new UserRepresentation();
        CredentialRepresentation creds = new CredentialRepresentation();
        creds.setTemporary(false);
        creds.setType(CredentialRepresentation.PASSWORD);
        creds.setValue(req.getCredentials().getPassword().value);
        newUser.setEnabled(true);
        newUser.setUsername(req.getProfile().username);
        newUser.setFirstName(req.getProfile().firstName);
        newUser.setLastName(req.getProfile().lastName);
        newUser.setEmail(req.getProfile().email);
        newUser.setCredentials(List.of(creds));
        newUser.setRealmRoles(List.of("ROLE_USER"));
        if (req.isAdmin)
            newUser.setGroups(List.of("Admins"));

        keycloak.realm("LibraryIdentityProvider")
                .users()
                .create(newUser);

        ResponseEntity<String> res = ResponseEntity.status(HttpStatus.CREATED).build();

        try {
            producer.sendMessage(
                    new StatMessage(
                            req.getProfile().username, startDate, LocalDateTime.now(), ActionType.REGISTRATION, req));
        } catch (JsonProcessingException e) {
            log.error("[AUTH] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }
}
