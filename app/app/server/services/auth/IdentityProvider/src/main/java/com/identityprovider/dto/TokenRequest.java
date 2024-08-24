package com.identityprovider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenRequest {
    @JsonProperty(value = "authentication_code")
    public String authenticationCode;

    @JsonProperty(value = "client_id")
    public String clientId;

    @JsonProperty(value = "client_secret")
    public String clientSecret;
}
