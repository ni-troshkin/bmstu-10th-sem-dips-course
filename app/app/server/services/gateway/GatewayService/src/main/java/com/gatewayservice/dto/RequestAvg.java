package com.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RequestAvg {
    public String service;
    public String action;
    public long num;
    public double avgTime;
}
