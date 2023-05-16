package com.atanava.bsc.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TriangleDto {
    BaseStationDto pointA;
    BaseStationDto pointB;
    BaseStationDto pointC;
}
