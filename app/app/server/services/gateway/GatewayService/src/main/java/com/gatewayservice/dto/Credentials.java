package com.gatewayservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class Credentials {
    @JsonProperty(value = "password")
    public Password password;

    @ToString
    public static class Password {
        @JsonProperty(value = "value")
        public String value;
    }
}