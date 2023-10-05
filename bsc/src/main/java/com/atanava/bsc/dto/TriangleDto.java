package com.atanava.bsc.dto;

import lombok.Builder;

@Builder
public record TriangleDto(BaseStationDto pointA,
                          BaseStationDto pointB,
                          BaseStationDto pointC) { }
