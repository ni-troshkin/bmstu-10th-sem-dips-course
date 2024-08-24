package com.identityprovider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class TokenKeycloakRequest {
    public MultiValueMap<String, String> map;

    public TokenKeycloakRequest(String clientId, String clientSecret, String code) {
        map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("code", code);
        map.add("redirect_uri", "http%3A%2F%2Flocalhost%3A8090%2Fapi%2Fv1%2Fmanage%2Fhealth");
    }

    public TokenKeycloakRequest(TokenRequest req) {
        new TokenKeycloakRequest(req.getClientId(), req.getClientSecret(), req.getAuthenticationCode());
    }
}
