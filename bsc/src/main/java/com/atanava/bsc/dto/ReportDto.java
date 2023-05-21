package com.atanava.bsc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.sql.Timestamp;
import java.util.UUID;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDto {
    @JsonProperty("mobile_station_id")
    UUID mobileId;
    Float distance;
    Timestamp timestamp;
}
