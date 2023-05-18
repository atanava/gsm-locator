package com.atanava.bts.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.awt.geom.Ellipse2D;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(exclude = {"detectionArea"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseStation {

    private final UUID id;

    private final String name;

    private final Float xCoordinate;

    private final Float yCoordinate;

    private final Float detectionRadiusInMeters;

    @JsonIgnore
    private final Ellipse2D.Float detectionArea;

    @JsonCreator
    public BaseStation(UUID id, String name, Float xCoordinate, Float yCoordinate, Float detectionRadiusInMeters) {
        this.id = id;
        this.name = name;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.detectionRadiusInMeters = detectionRadiusInMeters;
        this.detectionArea = computeArea();
    }

    private Ellipse2D.Float computeArea() {
        return new Ellipse2D.Float(
                xCoordinate - detectionRadiusInMeters,
                yCoordinate + detectionRadiusInMeters,
                detectionRadiusInMeters * 2,
                detectionRadiusInMeters * 2
                );
    }
}
