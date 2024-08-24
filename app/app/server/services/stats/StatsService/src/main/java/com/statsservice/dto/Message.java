package com.statsservice.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Accessors(chain = true)
@ToString
public class Message {
    UUID eventUuid;
    String username;
    String action;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime eventStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime eventEnd;
    Map<String, Object> params;

    @JsonAnySetter
    void setParams(String key, Object value) {
        params.put(key, value);
    }
    String service;

    public Message(UUID eventUuid, String username, String action,
                   LocalDateTime eventStart, LocalDateTime eventEnd, String service) {
        this.eventUuid = eventUuid;
        this.username = username;
        this.action = action;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.service = service;
    }
}
