package com.atanava.bsc.helpers;

import com.atanava.bsc.service.data.BaseStationDefinition;
import com.atanava.bsc.service.data.Triangle;
import com.atanava.bsc.util.GeometryUtil;

import java.awt.geom.Point2D;
import java.util.UUID;

public class TestData {

    public static final UUID ID_A = UUID.randomUUID();
    public static final UUID ID_B = UUID.randomUUID();
    public static final UUID ID_C = UUID.randomUUID();
    public static final UUID ID_INNER = UUID.randomUUID();
    public static final UUID ID_OUTER = UUID.randomUUID();

    private static final float MAX_DETECTION = (float) GeometryUtil.distanceInMeters(new Point2D.Double(0, 0), new Point2D.Double(0, 8));
    public static final Point2D.Double POINT_A = new BaseStationDefinition(ID_A, 12.0, 10.0, "A", MAX_DETECTION);
    public static final Point2D.Double POINT_B = new BaseStationDefinition(ID_B, 21.0, 15.0, "A", MAX_DETECTION);
    public static final Point2D.Double POINT_C = new BaseStationDefinition(ID_A, 14.0, 22.0, "A", MAX_DETECTION);
    public static final Point2D.Double INNER_POINT = new BaseStationDefinition(ID_A, 17.0, 16.0, "A", MAX_DETECTION);
    public static final Point2D.Double OUTER_POINT = new BaseStationDefinition(ID_A, 23.0, 19.0, "A", MAX_DETECTION);

    public static final Triangle TRIANGLE = Triangle.builder()
            .idA(ID_A).pointA(POINT_A)
            .idB(ID_B).pointB(POINT_B)
            .idC(ID_C).pointC(POINT_C)
            .build();

}
