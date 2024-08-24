package com.gatewayservice.service;

import com.gatewayservice.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    private final RestTemplate restTemplate;
    private final String serverUrl;

    public AuthService(RestTemplate restTemplate,
                          @Value("${auth.server.url}") String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    public ResponseEntity<String> authorize(String client_id) {
        HttpEntity<AuthRequest> entity = new HttpEntity<>(new AuthRequest(client_id));
        ResponseEntity<String> auth = null;
        try {
            auth = restTemplate.exchange(
                            serverUrl + "/api/v1/oauth/auth",
                            HttpMethod.POST,
                            entity,
                            new ParameterizedTypeReference<String>() {
                            }
            );

            /*;*/
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return auth;
    }

    public ResponseEntity<TokenResponse> token(TokenRequest tokenRequest) {
        HttpEntity<TokenRequest> entity = new HttpEntity<>(tokenRequest);
        ResponseEntity<TokenResponse> token = null;
        try {
            token = restTemplate.exchange(
                    serverUrl + "/api/v1/oauth/token",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<TokenResponse>() {
                    }
            );

            /*;*/
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return token;
    }

    public ResponseEntity<String> logout(String username) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<String> res = null;
        try {
            res = restTemplate.exchange(
                    serverUrl + "/api/v1/oauth/logout?username=" + username,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<String>() {
                    }
            );

            /*;*/
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return res;
    }

    public ResponseEntity<String> register(RegisterRequestDto req) {
        HttpEntity<RegisterRequestDto> entity = new HttpEntity<>(req);
        ResponseEntity<String> res = null;
        try {
            res = restTemplate.exchange(
                    serverUrl + "/api/v1/oauth/register",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<String>() {
                    }
            );

            /*;*/
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return res;
    }
}
