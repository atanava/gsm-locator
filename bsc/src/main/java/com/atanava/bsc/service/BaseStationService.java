package com.atanava.bsc.service;

import com.atanava.bsc.dto.LastKnownLocation;
import com.atanava.bsc.dto.MessageDto;
import com.atanava.bsc.error.FakeReportException;
import com.atanava.bsc.error.NotFoundException;
import com.atanava.bsc.model.BaseStation;
import com.atanava.bsc.model.Report;
import com.atanava.bsc.repository.BaseStationRepository;
import com.atanava.bsc.service.data.BaseStationDefinition;
import com.atanava.bsc.service.data.BaseStationMesh;
import com.atanava.bsc.service.data.Triangle;
import com.atanava.bsc.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseStationService {

    private final ReportService reportService;

    private final BcsCacheHolder meshHolder;

    private final BaseStationRepository baseStationRepository;

    public Mono<MessageDto> saveReports(MessageDto messageDto){
        return baseStationRepository.findById(messageDto.getBaseId())
                .switchIfEmpty(Mono.error(new FakeReportException(messageDto.getBaseId())))
                .flatMap(base -> reportService.saveReports(messageDto))
                .onErrorResume(e -> {
                    log.error(e.getMessage() + System.lineSeparator() + "Report Message: " + messageDto);
                    return Mono.just(messageDto);
                });
    }

    public Mono<BaseStation> save(BaseStation baseStation) {
        return baseStationRepository.save(baseStation);
    }

    public Mono<LastKnownLocation> findLastLocation(UUID mobileId, Pageable pageable) {
        BaseStationMesh baseStationMesh = meshHolder.getBaseStationMesh();
        return reportService.findLastReports(mobileId, pageable)
                .switchIfEmpty(Mono.error(new NotFoundException("Reports were not found for mobileId: " + mobileId)))
                .collectList()
                .flatMap(list -> Flux.fromIterable(list)
                        .filter(report -> mobileId.equals(report.getMobileId()))
                        .flatMap(report -> Flux.fromIterable(baseStationMesh.getTriangles())
                                .filter(triangle -> triangle.getVertexes().containsKey(report.getBaseId()))
                                .flatMap(triangle -> getOtherReports(list, report, triangle)
                                        .collectList()
                                        .map(reports -> {
                                            if (reports.size() < 1) {
                                                log.error(prepareError(mobileId, -1F).toString());
                                                return meshHolder.getLastKnownLocations().stream()
                                                        .filter(loc -> mobileId.equals(loc.getMobileId()))
                                                        .findFirst()
                                                        .orElse(prepareError(mobileId, reports.get(0).getDistance()));
                                            }
                                            reports.add(report);
                                            return updateLastLocation(mobileId, triangle, reports);
                                        })
                                )
                        )
                        .next()
                        .switchIfEmpty(Mono.error(new RuntimeException()))
                        .onErrorResume((e) -> {
                            log.error(e.getMessage());
                            return Mono.just(prepareError(mobileId, -1F));
                        })
                );
    }

    private Flux<Report> getOtherReports(List<Report> list, Report report, Triangle triangle) {
        return Flux.fromIterable(triangle.getVertexes().keySet())
                .filter(baseId -> !baseId.equals(report.getBaseId()))
                .map(baseId -> list.stream()
                        .filter(rep -> baseId.equals(rep.getBaseId()))
                        .max(Comparator.comparing(Report::getTimestamp))
                        .orElse(report))
                .distinct(Report::getBaseId)
                .take(2);
    }

    private LastKnownLocation prepareError(UUID mobileId, float distance) {
        return LastKnownLocation.builder()
                .mobileId(mobileId)
                .errorRadius(distance)
                .errorCode(HttpStatus.NOT_FOUND.value())
                .errorDescription("Unable to find location")
                .build();
    }

    private LastKnownLocation updateLastLocation(UUID mobileId, Triangle triangle, List<Report> reports) {
        return GeometryUtil.computeLocation(
                        triangle,
                        reports.stream()
                                .collect(Collectors.toMap(Report::getBaseId, (r) -> r.getDistance().doubleValue()))
                )
                .map(point -> {
                    LastKnownLocation lastKnownLocation = prepareLastLocation(mobileId, triangle, point);
                    meshHolder.getLastKnownLocations().add(lastKnownLocation);
                    return lastKnownLocation;
                })
                .orElse(prepareError(mobileId, -1F));
    }

    private LastKnownLocation prepareLastLocation(UUID mobileId, Triangle triangle, Point2D point) {
        BaseStationDefinition pointA = (BaseStationDefinition) triangle.getPointA();
        BaseStationDefinition pointB = (BaseStationDefinition) triangle.getPointB();
        BaseStationDefinition pointC = (BaseStationDefinition) triangle.getPointC();

        double distanceA = GeometryUtil.distanceInMeters(point, pointA);
        double distanceB = GeometryUtil.distanceInMeters(point, pointB);
        double distanceC = GeometryUtil.distanceInMeters(point, pointC);

        if (distanceA > pointA.getDetectionRadiusInMeters()) {
            return prepareError(mobileId, (float) distanceA);
        }
        if (distanceB > pointB.getDetectionRadiusInMeters()) {
            return prepareError(mobileId, (float) distanceB);
        }
        if (distanceC > pointA.getDetectionRadiusInMeters()) {
            return prepareError(mobileId, (float) distanceA);
        }
        return LastKnownLocation.builder()
                .mobileId(mobileId)
                .x((float) point.getX())
                .y((float) point.getY())
                .build();
    }

}
