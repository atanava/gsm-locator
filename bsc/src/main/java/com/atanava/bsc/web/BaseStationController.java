package com.atanava.bsc.web;

import com.atanava.bsc.dto.LastKnownLocation;
import com.atanava.bsc.dto.MessageDto;
import com.atanava.bsc.service.BaseStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BaseStationController {

    private final BaseStationService baseStationService;

    @PostMapping("/report")
    public Mono<MessageDto> saveReports(@RequestBody MessageDto messageDto) {
        return baseStationService.saveReports(messageDto);
    }

    @GetMapping("/location/{uuid}")
    public Mono<LastKnownLocation> getMobileLocation(@PathVariable UUID uuid) {
        return baseStationService.findLastLocation(uuid, Pageable.ofSize(16).withPage(1));
    }

}
