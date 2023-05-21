package com.atanava.bsc.service.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseStationMesh {
    Map<UUID, BaseStationDefinition> points;
    SortedSet<Triangle> triangles;
}
