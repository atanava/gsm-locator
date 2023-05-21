package com.atanava.bsc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"mobileId"})
public class LastKnownLocation implements Comparable<LastKnownLocation> {
    UUID mobileId;
    float x;
    float y;
    @JsonProperty("error_radius")
    float errorRadius;
    @JsonProperty("error_code")
    int errorCode;
    @JsonProperty("error_description")
    String errorDescription;

    @Override
    public int compareTo(LastKnownLocation that) {
        return this.mobileId.compareTo(that.mobileId);
    }
}
