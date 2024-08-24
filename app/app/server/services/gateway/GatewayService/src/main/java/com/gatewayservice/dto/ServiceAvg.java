package com.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceAvg {
    public String service;
    public long num;
    public double avgTime;
}
