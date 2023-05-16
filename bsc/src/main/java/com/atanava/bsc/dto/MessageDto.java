package com.atanava.bsc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Collection;
import java.util.UUID;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDto {
    @JsonProperty("base_station_id")
    UUID baseId;
    @JsonProperty("reports")
    Collection<ReportDto> reports;
}
