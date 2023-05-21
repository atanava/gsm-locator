package com.atanava.bsc.util;

import com.atanava.bsc.service.data.Triangle;

import java.awt.geom.*;
import java.util.*;
import java.util.stream.Collectors;

public class GeometryUtil {

    public static boolean checkPointEnteringByTriangle(Triangle triangle, Point2D point) {
        Double x0 = point.getX();
        Double x1 = triangle.getPointA().getX();
        Double x2 = triangle.getPointB().getX();
        Double x3 = triangle.getPointC().getX();

        Double y0 = point.getY();
        Double y1 = triangle.getPointA().getY();
        Double y2 = triangle.getPointB().getY();
        Double y3 = triangle.getPointC().getY();

        double v1 = (x1 - x0) * (y2 - y1) - (x2 - x1) * (y1 - y0);
        double v2 = (x2 - x0) * (y3 - y2) - (x3 - x2) * (y2 - y0);
        double v3 = (x3 - x0) * (y1 - y3) - (x1 - x3) * (y3 - y0);

        return (v1 >= 0 && v2 >= 0 && v3 >= 0) || (v1 < 0 && v2 < 0 && v3 < 0);
    }

    public static Optional<Point2D> computeLocation(Triangle triangle, Map<UUID, Double> distances) {
        List<Area> intersections = getIntersections(triangle, distances);
        Area left = intersections.stream().min(Comparator.comparingDouble(
                a -> a.getBounds2D().getMinX())).orElseThrow(() -> new RuntimeException("Min-X is not found")
        );
        Area bottom = intersections.stream().min(Comparator.comparingDouble(
                a -> a.getBounds2D().getMinY())).orElseThrow(() -> new RuntimeException("Min-Y is not found")
        );
        Point2D.Double result = new Point2D.Double(left.getBounds2D().getMaxX(), bottom.getBounds2D().getMaxY());

        return checkPointEnteringByTriangle(triangle, result) ? Optional.of(result) : Optional.empty();
    }

    private static List<Area> getIntersections(Triangle triangle, Map<UUID, Double> distances) {
        List<Area> circles = getCircles(triangle, distances);
        if (circles.size() < 2) {
            return Collections.emptyList();
        }
        List<Area> intersections = new ArrayList<>();
        Area circleA = circles.get(0);
        Area circleB = circles.get(1);

        Area cloneA = (Area) circleA.clone();
        Area cloneB = (Area) circleB.clone();

        circleA.intersect(cloneB);
        intersections.add(circleA);

        if (circles.size() == 3) {
            Area circleC = circles.get(2);
            Area cloneC = (Area) circleC.clone();

            circleB.intersect(cloneC);
            circleC.intersect(cloneA);

            intersections.add(circleB);
            intersections.add(circleC);
        }
        return intersections;
    }

    private static List<Area> getCircles(Triangle triangle, Map<UUID, Double> distances) {
        return distances.entrySet().stream()
                .filter(dist -> triangle.getVertexes().get(dist.getKey()) != null)
                .map(dist -> createCircle(dist.getValue(), triangle.getVertexes().get(dist.getKey())))
                .collect(Collectors.toList());
    }

    private static Area createCircle(Double radius, Point2D point) {
        double leftX = point.getX() - radius;
        double bottomY = point.getY() - radius;
        Ellipse2D.Double aCircle = new Ellipse2D.Double(leftX, bottomY, radius*2, radius*2);

        return new Area(aCircle);
    }

    public static Set<Triangle> getTriangles(Map<UUID, Point2D> points) {
        if ((points != null) && (points.size() > 0)) {
            int n = points.size();

            double[] x = new double[n];
            double[] y = new double[n];
            double[] z = new double[n];
            UUID[] ids = new UUID[n];

            int i = 0;

            for (Map.Entry<UUID, Point2D> point : points.entrySet()) {
                x[i] = point.getValue().getX();
                y[i] = point.getValue().getY();
                z[i] = (x[i] * x[i] + y[i] * y[i]);
                ids[i] = point.getKey();

                i++;
            }
            return getTriangles(n, x, y, z, ids, points);
        }
        return null;
    }

    public static Set<Triangle> getTriangles(int n, double[] x, double[] y, double[] z, UUID[] ids, Map<UUID, Point2D> points) {
        Set<Triangle> triangles = new HashSet<>();
        if (n == 2) {
            return Collections.emptySet();
        }
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = i + 1; k < n; k++) {
                    if (j != k) {
                        boolean znIsLessThanZero = isZnLessThanZero(n, x, y, z, i, j, k);
                        if (znIsLessThanZero) {
                            triangles.add(Triangle.builder()
                                            .idA(ids[i]).pointA(points.get(ids[i]))
                                            .idB(ids[j]).pointB(points.get(ids[j]))
                                            .idC(ids[k]).pointC(points.get(ids[k]))
                                    .build());
                        }
                    }
                }
            }
        }
        return triangles;
    }

    private static boolean isZnLessThanZero(int n, double[] x, double[] y, double[] z, int i, int j, int k) {
        double xn = (y[j] - y[i]) * (z[k] - z[i]) - (y[k] - y[i]) * (z[j] - z[i]);
        double yn = (x[k] - x[i]) * (z[j] - z[i]) - (x[j] - x[i]) * (z[k] - z[i]);
        double zn = (x[j] - x[i]) * (y[k] - y[i]) - (x[k] - x[i]) * (y[j] - y[i]);

        boolean znIsLessThanZero = zn < 0;
        if (znIsLessThanZero) {
            for (int m = 0; m < n; m++) {
                znIsLessThanZero = znIsLessThanZero && ((x[m] - x[i]) * xn + (y[m] - y[i]) * yn + (z[m] - z[i]) * zn <= 0);
            }
        }
        return znIsLessThanZero;
    }

//    https://stackoverflow.com/a/16794680
    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @return Distance in Meters
     */
    //TODO: Try to use some constants
    public static double distanceInMeters(double lat1, double lat2, double lon1,
                                          double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public static double distanceInMeters(Point2D p1, Point2D p2) {
        return distanceInMeters(p1.getX(), p2.getX(), p1.getY(), p2.getY(), 0, 0);
    }

}
