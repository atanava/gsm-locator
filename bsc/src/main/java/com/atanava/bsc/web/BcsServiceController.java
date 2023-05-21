package com.atanava.bsc.web;

import com.atanava.bsc.dto.TriangleDto;
import com.atanava.bsc.service.BcsCacheHolder;
import com.atanava.bsc.util.BaseStationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class BcsServiceController {

    private final BcsCacheHolder bcsCacheHolder;

    @GetMapping("/service")
    public Flux<TriangleDto> getBaseStationMesh() {
        return Flux.fromIterable(bcsCacheHolder.getBaseStationMesh().getTriangles())
                .map(BaseStationConverter::triangleToDto);
    }
}
