package com.atanava.bsc.service;

import com.atanava.bsc.dto.LastKnownLocation;
import com.atanava.bsc.service.data.BaseStationMesh;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class BcsCacheHolder {

    @Getter
    private final BaseStationMesh baseStationMesh;

    @Getter
    private final NavigableSet<LastKnownLocation> lastKnownLocations;


    public BcsCacheHolder() {
        this(new BaseStationMesh(
                new ConcurrentHashMap<>(), new ConcurrentSkipListSet<>(Comparator.comparingInt(Objects::hashCode))),
                new ConcurrentSkipListSet<>());
    }

    private BcsCacheHolder(BaseStationMesh baseStationMesh, NavigableSet<LastKnownLocation> lastKnownLocations) {
        this.baseStationMesh = baseStationMesh;
        this.lastKnownLocations = lastKnownLocations;
    }
}
