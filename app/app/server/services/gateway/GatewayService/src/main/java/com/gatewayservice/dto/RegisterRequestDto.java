package com.gatewayservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterRequestDto {
    @JsonProperty(value = "profile")
    public ProfileDto profile;

    @JsonProperty(value = "credentials")
    public Credentials credentials;

    @JsonProperty(value = "is_admin")
    public boolean isAdmin;

}

