package com.atanava.bsc.util;

import com.atanava.bsc.service.data.Triangle;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static com.atanava.bsc.helpers.TestData.*;

class GeometryUtilTest {

    @Test
    void checkPointEntering() {
        assertTrue(GeometryUtil.checkPointEnteringByTriangle(TRIANGLE, INNER_POINT));

        assertFalse(GeometryUtil.checkPointEnteringByTriangle(TRIANGLE, OUTER_POINT));
    }

    @Test
    void computeLocation() {
        Map<UUID, Double> distances = new HashMap<UUID, Double>() {{
            put(ID_A, POINT_A.distance(INNER_POINT));
            put(ID_B, POINT_B.distance(INNER_POINT));
            put(ID_C, POINT_C.distance(INNER_POINT));
        }};
        Point2D point2D = GeometryUtil.computeLocation(TRIANGLE, distances).get();
        assertNotNull(point2D);
        System.out.println(point2D);

        double actualX = (double) ((int) (point2D.getX() * 100))  / 100;
        double actualY = (double) ((int) (point2D.getY() * 100)) / 100;
        double innerPointX = (double) ((int) (INNER_POINT.getX() * 100))  / 100;
        double innerPointY = (double) ((int) (INNER_POINT.getY() * 100)) / 100;

        assertEquals(innerPointX, actualX);
        assertEquals(innerPointY, actualY);

        distances = new HashMap<UUID, Double>() {{
            put(ID_A, POINT_A.distance(OUTER_POINT));
            put(ID_B, POINT_B.distance(OUTER_POINT));
            put(ID_C, POINT_C.distance(OUTER_POINT));
        }};

        assertFalse(GeometryUtil.computeLocation(TRIANGLE, distances).isPresent());
    }

    @Test
    void getTriangles() {
        Set<Triangle> triangles = GeometryUtil.getTriangles(new HashMap<UUID, Point2D>() {{
            put(ID_A, POINT_A);
            put(ID_B, POINT_B);
            put(ID_C, POINT_C);
            put(ID_INNER, INNER_POINT);
            put(ID_OUTER, OUTER_POINT);
        }});
        assertEquals(4, triangles.size());
        triangles.forEach(triangle -> assertTrue(triangle.getVertexes().containsValue(INNER_POINT)));
        triangles.forEach(triangle -> assertTrue(triangle.getVertexes().containsKey(ID_INNER)));

        Set<UUID> expectedIds = triangles.stream()
                .flatMap(triangle -> triangle.getVertexes().keySet().stream())
                .collect(Collectors.toSet());
        assertEquals(5, expectedIds.size());
        assertTrue(expectedIds.containsAll(Arrays.asList(ID_A, ID_B, ID_C, ID_INNER, ID_OUTER)));

        Set<Point2D> expectedPoints = triangles.stream()
                .flatMap(triangle -> triangle.getVertexes().values().stream())
                .collect(Collectors.toSet());
        assertEquals(5, expectedPoints.size());
        assertTrue(expectedPoints.containsAll(Arrays.asList(POINT_A, POINT_B, POINT_C, INNER_POINT, OUTER_POINT)));
    }

}
