package com.atanava.bsc.repository;

import com.atanava.bsc.model.BaseStation;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BaseStationRepository extends ReactiveCrudRepository<BaseStation, UUID> {

    @Modifying
    @Query("update base_station set radius=:radius where id=:id")
    Mono<Integer> updateBaseStationRadius(UUID id, float radius);
}
