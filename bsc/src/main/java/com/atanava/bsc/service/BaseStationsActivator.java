package com.atanava.bsc.service;

import com.atanava.bsc.service.data.BaseStationDefinition;
import com.atanava.bsc.util.BaseStationConverter;
import com.atanava.bsc.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseStationsActivator {

    @Value("${base.detection.averageDistance}")
    private float averageDistance;

    private final BaseStationDefinitionProvider stationDefinitionProvider;

    private final BaseStationService baseStationService;

    private final TransactionalOperator txOperator;

    private final BcsCacheHolder meshHolder;

    public Mono<Integer> activateStations() {
        return stationDefinitionProvider.findFakeBaseStations()
                .flatMap(dto -> baseStationService.save(
                                        BaseStationConverter.dtoToBaseStation(dto)
                                                .toBuilder()
                                                .detectionRadiusInMeters(averageDistance).build()
                                )
                                .map(BaseStationConverter::baseStationToDefinition)
                )
                .as(txOperator::transactional)
                .collectMap(BaseStationDefinition::getId, Function.identity())
                .map(map -> {
                    meshHolder.getBaseStationMesh().getPoints().putAll(map);
                    meshHolder.getBaseStationMesh().getTriangles().addAll(GeometryUtil.getTriangles(
                            map.entrySet().stream()
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                    ));
                    map.values().forEach(base -> log.info("Base Station activated {}", base.toString()));

                    return map.size();
                });
    }

}
