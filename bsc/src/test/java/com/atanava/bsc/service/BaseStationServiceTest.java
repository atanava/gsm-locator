package com.atanava.bsc.service;

import com.atanava.bsc.dto.LastKnownLocation;
import com.atanava.bsc.dto.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.atanava.bsc.helpers.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class BaseStationServiceTest {

    private BaseStationService service;

    @BeforeEach
    void setup() {
        BcsCacheHolder meshHolder = getBcsCacheHolder();
        ReportService reportService = Mockito.mock(ReportService.class);
        Mockito.when(reportService.saveReports(any(MessageDto.class))).thenReturn(Mono.just(MESSAGE_DTO_A_INNER));
        Mockito.when(reportService.findLastReports(any(UUID.class), any(Pageable.class))).thenReturn(getReports());

        service = new BaseStationService(reportService, meshHolder);
    }

    @Test
    void saveReports() {
        MessageDto actual = service.saveReports(MESSAGE_DTO_A_INNER).block();
        assertEquals(MESSAGE_DTO_A_INNER, actual);
    }

    @Test
    void findLastLocation_Ok() {
        LastKnownLocation lastKnownLocation = service.findLastLocation(ID_INNER, Pageable.ofSize(16).withPage(1)).block();
        assertNotNull(lastKnownLocation);
        assertEquals(ID_INNER, lastKnownLocation.getMobileId());
        assertTrue(lastKnownLocation.getX() > 0);
        assertTrue(lastKnownLocation.getY() > 0);
        assertEquals(0, lastKnownLocation.getErrorCode());
        assertEquals(0, lastKnownLocation.getErrorRadius());
        assertNull(lastKnownLocation.getErrorDescription());
        System.out.println(lastKnownLocation);
    }

    @Test
    void findLastLocation_NotFound() {
        LastKnownLocation lastKnownLocation = service.findLastLocation(ID_OUTER, Pageable.ofSize(16).withPage(1)).block();
        assertNotNull(lastKnownLocation);
        assertEquals(ID_OUTER, lastKnownLocation.getMobileId());
        assertEquals(0, lastKnownLocation.getX());
        assertEquals(0, lastKnownLocation.getY());
        assertEquals(404, lastKnownLocation.getErrorCode());
        assertEquals(-1, lastKnownLocation.getErrorRadius());
        assertNotNull(lastKnownLocation.getErrorDescription());
        System.out.println(lastKnownLocation);
    }
}