package com.atanava.bsc.service;

import com.atanava.bsc.dto.BaseStationDto;
import com.atanava.bsc.util.OsmUriHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class BaseStationDefinitionProvider {

    @Value("${osm.uri.basic}")
    private String uri;
    @Value("${osm.uri.params.type}")
    private String[] types;
    @Value("${osm.uri.params.location}")
    private String location;
    @Value("${osm.uri.params.format}")
    private String format;
    @Value("${base.mesh.maxSize}")
    private int maxMeshSize;

    private final WebClient webClient;

    public Flux<BaseStationDto> findFakeBaseStations() {
        return Flux.concat(createRequests()).doOnNext(base -> log.info("Fake Base Station received: {} {}", base.getLocationType(), base.getName()));
    }

    private Iterable<Flux<BaseStationDto>> createRequests() {
        int batchSize = maxMeshSize / types.length;
        int iter = maxMeshSize / batchSize;
        AtomicInteger remained = new AtomicInteger(maxMeshSize % types.length);

        return IntStream.range(0, iter)
                .mapToObj(i -> OsmUriHelper.builder()
                        .baseUri(uri).type(types[i]).location(location).format(format)
                        .limit(getLimit(batchSize, remained))
                        .build()
                )
                .map(OsmUriHelper::prepareUri)
                .map(uri -> {
                    log.info("Trying to GET from URL: {}", uri);
                    return webClient.method(HttpMethod.GET)
                            .uri(uri)
                            .retrieve()
                            .bodyToFlux(BaseStationDto.class);
                })
                .collect(Collectors.toSet());
    }

    private int getLimit(int batchSize, final AtomicInteger remained) {
        return batchSize + (remained.getAndDecrement() > 0 ? 1 : 0);
    }

}
