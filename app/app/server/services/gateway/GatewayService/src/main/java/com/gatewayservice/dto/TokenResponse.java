package com.gatewayservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
@JsonIgnoreProperties
public class TokenResponse {
    @JsonProperty(value = "access_token")
    public String accessToken;

    @JsonProperty(value = "expires_in")
    public Integer expiresIn;

    @JsonProperty(value = "refresh_expires_in")
    public Integer refreshExpiresIn;

    @JsonProperty(value = "refresh_token")
    public String refreshToken;

    @JsonProperty(value = "token_type")
    public String tokenType;

    @JsonProperty(value = "id_token")
    public String idToken;

    @JsonProperty(value = "not_before_policy")
    public Integer notBeforePolicy;

    @JsonProperty(value = "session_state")
    public String sessionState;

    @JsonProperty(value = "scope")
    public String scope;
}
