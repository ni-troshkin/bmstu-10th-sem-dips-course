package com.ratingservice.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@ToString
public class StatMessage {
    UUID eventUuid;
    String username;
    String action;
    LocalDateTime eventStart;
    LocalDateTime eventEnd;
    Map<String, Object> params;

    @JsonAnySetter
    void setParams(String key, Object value) {
        params.put(key, value);
    }
    String service;

    public StatMessage(String username, LocalDateTime eventStart, LocalDateTime eventEnd,
                       String action, Object params) {
        ObjectMapper mapper = new ObjectMapper();

        this.eventUuid = UUID.randomUUID();
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.username = username;
        this.action = action;
        this.params = mapper.convertValue(params, Map.class);
        this.service = "RATING";
    }
}
