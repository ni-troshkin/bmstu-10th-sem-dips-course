package com.gatewayservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {
    String access_token;
    int expires_in;
    int refresh_expires_in;
    String refresh_token;
    String token_type;
    String id_token;

    @JsonProperty(value = "not-before-policy")
    int not_before_policy;

    String session_state;
    String scope;
    String username;

    public TokenResponseDto(TokenResponse token, String username) {
        this.access_token = token.getAccess_token();
        this.expires_in = token.getExpires_in();
        this.refresh_expires_in = token.getRefresh_expires_in();
        this.refresh_token = token.getRefresh_token();
        this.token_type = token.getToken_type();
        this.id_token = token.getId_token();
        this.not_before_policy = token.getNot_before_policy();
        this.session_state = token.getSession_state();
        this.scope = token.getScope();
        this.username = username;
    }
}
