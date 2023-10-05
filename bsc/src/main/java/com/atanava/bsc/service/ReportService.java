package com.atanava.bsc.service;

import com.atanava.bsc.dto.MessageDto;
import com.atanava.bsc.model.Report;
import com.atanava.bsc.repository.ReportRepository;
import com.atanava.bsc.util.ReportConverter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    private final TransactionalOperator txOperator;

    public Mono<MessageDto> saveReports(MessageDto messageDto) {
        val baseId = messageDto.baseId();
        return Flux.fromIterable(messageDto.reports())
                .map(dto -> reportRepository.save(ReportConverter.dtoToReport(baseId, dto)))
                .then()
                .thenReturn(messageDto)
                .as(txOperator::transactional);
    }

    public Flux<Report> findLastReports(UUID mobileId, Pageable pageable) {
        return reportRepository.findAllByMobileIdOrderByTimestampDesc(mobileId, pageable);
    }

}
