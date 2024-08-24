package com.gatewayservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProfileDto {
    @JsonProperty(value = "firstName")
    public String firstName;

    @JsonProperty(value = "lastName")
    public String lastName;

    @JsonProperty(value = "email")
    public String email;

    @JsonProperty(value = "username")
    public String username;

    @JsonProperty(value = "mobilePhone")
    public String mobilePhone;

    @JsonProperty(value = "userType")
    public String userType = "user";
}
