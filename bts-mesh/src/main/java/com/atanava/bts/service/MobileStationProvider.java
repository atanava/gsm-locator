package com.atanava.bts.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MobileStationProvider {

    public List<MobileStation> findMobiles() {
        return new CopyOnWriteArrayList<>();
    }
}
