package com.atanava.bsc.service.data;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.UUID;

@Getter
@ToString
@Value
@Builder(toBuilder = true)
public class BaseStationDefinition extends Point2D.Double implements Comparable<Point2D.Double> {
    public static final Comparator<Point2D.Double> COMPARATOR = Comparator.comparingDouble(o -> o.getX() + o.getY());
    UUID id;
    java.lang.Double longitude;
    java.lang.Double latitude;
    String name;
    java.lang.Float detectionRadiusInMeters;

    public BaseStationDefinition(UUID id,
                                 java.lang.Double longitude,
                                 java.lang.Double latitude,
         String name, java.lang.Float detectionRadiusInMeters) {
        /** NB! Longitude = X Latitude = Y*/
        super(longitude, latitude);
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.detectionRadiusInMeters = detectionRadiusInMeters;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(Point2D.Double that) {
        return COMPARATOR.compare(this, that);
    }
}
