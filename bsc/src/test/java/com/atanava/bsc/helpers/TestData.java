package com.atanava.bsc.helpers;

import com.atanava.bsc.dto.LastKnownLocation;
import com.atanava.bsc.dto.MessageDto;
import com.atanava.bsc.dto.ReportDto;
import com.atanava.bsc.model.Report;
import com.atanava.bsc.service.BcsCacheHolder;
import com.atanava.bsc.service.data.BaseStationDefinition;
import com.atanava.bsc.service.data.BaseStationMesh;
import com.atanava.bsc.service.data.Triangle;
import com.atanava.bsc.util.GeometryUtil;
import com.atanava.bsc.util.ReportConverter;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;

import java.awt.geom.Point2D;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class TestData {


    public static final UUID ID_A = UUID.randomUUID();
    public static final UUID ID_B = UUID.randomUUID();
    public static final UUID ID_C = UUID.randomUUID();
    public static final UUID ID_INNER = UUID.randomUUID();
    public static final UUID ID_OUTER = UUID.randomUUID();

    private static final float MAX_DETECTION = (float) GeometryUtil.distanceInMeters(new Point2D.Double(0, 0), new Point2D.Double(0, 8));
    public static final Point2D.Double POINT_A = new BaseStationDefinition(ID_A, 12.0, 10.0, "A", MAX_DETECTION);
    public static final Point2D.Double POINT_B = new BaseStationDefinition(ID_B, 21.0, 15.0, "B", MAX_DETECTION);
    public static final Point2D.Double POINT_C = new BaseStationDefinition(ID_C, 14.0, 22.0, "C", MAX_DETECTION);
    public static final Point2D.Double INNER_POINT = new BaseStationDefinition(ID_INNER, 17.0, 16.0, "INNER", MAX_DETECTION);
    public static final Point2D.Double OUTER_POINT = new BaseStationDefinition(ID_OUTER, 23.0, 19.0, "OUTER", MAX_DETECTION);

    public static final ReportDto REPORT_DTO_A_INNER = new ReportDto(ID_INNER, (float) INNER_POINT.distance(POINT_A), getTimestamp());
    public static final ReportDto REPORT_DTO_B_INNER = new ReportDto(ID_INNER, (float) INNER_POINT.distance(POINT_B), getTimestamp());
    public static final ReportDto REPORT_DTO_C_INNER = new ReportDto(ID_INNER, (float) INNER_POINT.distance(POINT_C), getTimestamp());
    public static final ReportDto REPORT_DTO_A_OUTER = new ReportDto(ID_OUTER, (float) OUTER_POINT.distance(POINT_A), getTimestamp());
    public static final ReportDto REPORT_DTO_B_OUTER = new ReportDto(ID_OUTER, (float) OUTER_POINT.distance(POINT_B), getTimestamp());
    public static final ReportDto REPORT_DTO_C_OUTER = new ReportDto(ID_OUTER, (float) OUTER_POINT.distance(POINT_C), getTimestamp());

    public static final MessageDto MESSAGE_DTO_A_INNER = new MessageDto(ID_A, Collections.singletonList(REPORT_DTO_A_INNER));
    public static final MessageDto MESSAGE_DTO_B_INNER = new MessageDto(ID_B, Collections.singletonList(REPORT_DTO_B_INNER));
    public static final MessageDto MESSAGE_DTO_C_INNER = new MessageDto(ID_C, Collections.singletonList(REPORT_DTO_C_INNER));

    public static final MessageDto MESSAGE_DTO_A_OUTER = new MessageDto(ID_A, Collections.singletonList(REPORT_DTO_A_OUTER));
    public static final MessageDto MESSAGE_DTO_B_OUTER = new MessageDto(ID_B, Collections.singletonList(REPORT_DTO_B_OUTER));
    public static final MessageDto MESSAGE_DTO_C_OUTER = new MessageDto(ID_C, Collections.singletonList(REPORT_DTO_C_OUTER));

    public static final Triangle TRIANGLE = Triangle.builder()
            .idA(ID_A).pointA(POINT_A)
            .idB(ID_B).pointB(POINT_B)
            .idC(ID_C).pointC(POINT_C)
            .build();

    public static BcsCacheHolder getBcsCacheHolder() {
        BcsCacheHolder meshHolder = new BcsCacheHolder();
        meshHolder.getBaseStationMesh()
                .getTriangles()
                .add(TRIANGLE);
        Map<UUID, BaseStationDefinition> points = meshHolder.getBaseStationMesh().getPoints();
        points.put(ID_A, (BaseStationDefinition) POINT_A);
        points.put(ID_B, (BaseStationDefinition) POINT_B);
        points.put(ID_C, (BaseStationDefinition) POINT_C);
        meshHolder.getLastKnownLocations()
                .addAll(getLastLocations());
        return meshHolder;
    }

    public static List<LastKnownLocation> getLastLocations() {
        return Arrays.asList(
        LastKnownLocation.builder()
                .mobileId(UUID.randomUUID())
                .errorRadius(50)
                .errorCode(HttpStatus.NOT_FOUND.value())
                .errorDescription("Unable to find location")
                .build(),
        LastKnownLocation.builder()
                .mobileId(UUID.randomUUID())
                .x(33)
                .y(42)
                .build(),
        LastKnownLocation.builder()
                .mobileId(UUID.randomUUID())
                .x(18)
                .y(19)
                .build(),
        LastKnownLocation.builder()
                .mobileId(ID_INNER)
                .x((float) INNER_POINT.getX())
                .y((float) INNER_POINT.getY())
                .build()
        );
    }

    public static Flux<Report> getReports() {
        MessageDto[] messageDtos = {MESSAGE_DTO_A_INNER, MESSAGE_DTO_B_INNER, MESSAGE_DTO_C_INNER, MESSAGE_DTO_A_OUTER, MESSAGE_DTO_B_OUTER, MESSAGE_DTO_C_OUTER};
        return Flux.fromArray(messageDtos)
                .flatMap(message -> {
                    UUID baseId = message.baseId();
                    return Flux.fromStream(message.reports().stream()
                            .map(dto -> ReportConverter.dtoToReport(baseId, dto)));
                });
    }
    private static Timestamp getTimestamp() {
        return Timestamp.from(
                Instant.now().with(ChronoField.NANO_OF_SECOND, 0)
        );
    }

    public static final BaseStationMesh baseStationMesh = new BaseStationMesh(
            new ConcurrentHashMap<>(),
            new ConcurrentSkipListSet<>(Comparator.comparingInt(Objects::hashCode)));

}
