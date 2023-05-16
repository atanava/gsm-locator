package com.atanava.bsc.repository;

import com.atanava.bsc.model.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ReportRepository extends ReactiveCrudRepository<Report, UUID> {
    Flux<Report> findAllByMobileIdOrderByTimestampDesc(UUID mobileId, Pageable pageable);

    Flux<Report> findAllByBaseIdOrderByTimestampDesc(UUID mobileId, Pageable pageable);
}
