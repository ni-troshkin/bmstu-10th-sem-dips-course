package com.identityprovider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
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
}
