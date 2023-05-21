package com.atanava.bsc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name", "latitude", "longitude"})
public class BaseStationDto {
    @JsonProperty("id")
    String id = "";
    @JsonProperty("lat")
    Double latitude;
    @JsonProperty("lon")
    Double longitude;
    @JsonProperty("display_name")
    String name;
    @JsonProperty("type")
    String locationType;
    @JsonProperty("radius")
    Float detectionRadiusInMeters;

}
