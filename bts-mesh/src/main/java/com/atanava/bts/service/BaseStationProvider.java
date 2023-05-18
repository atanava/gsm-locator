package com.atanava.bts.service;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class BaseStationProvider {

    @Getter
    private final CopyOnWriteArrayList<BaseStation> BASE_STATIONS = new CopyOnWriteArrayList<>();
}
