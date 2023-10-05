package com.atanava.bsc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageDto(@JsonProperty("base_station_id") UUID baseId,
                         @JsonProperty("reports") Collection<ReportDto> reports) { }
