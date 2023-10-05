package com.atanava.bsc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReportDto(@JsonProperty("mobile_station_id") UUID mobileId,
                        Float distance,
                        Timestamp timestamp) { }
