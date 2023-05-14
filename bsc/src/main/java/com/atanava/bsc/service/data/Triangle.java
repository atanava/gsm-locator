package com.atanava.bsc.service.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(exclude = {"pointA", "pointB", "pointC", "vertexes"})
@ToString(exclude = {"idA", "idB", "idC"})
public class Triangle {
    private final UUID idA;
    private final Point2D pointA;
    private final UUID idB;
    private final Point2D pointB;
    private final UUID idC;
    private final Point2D pointC;

    Map<UUID, Point2D> vertexes;

    public Map<UUID, Point2D> getVertexes() {
        if (vertexes == null) {
            vertexes = new HashMap<>();
            putNonNulls(idA, pointA);
            putNonNulls(idB, pointB);
            putNonNulls(idC, pointC);
        }
        return Collections.unmodifiableMap(vertexes);
    }

    private void putNonNulls(UUID id, Point2D point) {
        if (id != null && point != null) {
            vertexes.put(id, point);
        }
    }

}