package com.atanava.bsc.web;

import com.atanava.bsc.dto.LastKnownLocation;
import com.atanava.bsc.dto.MessageDto;
import com.atanava.bsc.service.BaseStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.atanava.bsc.helpers.TestData.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseStationControllerTest {

    @MockBean
    private BaseStationService stationService;

    @Autowired
    private WebTestClient client;

    @Test
    void saveReports() {

        when(stationService.saveReports(any())).thenReturn(Mono.just(MESSAGE_DTO_A_INNER));
        client.post().uri("/report")
                .body(Mono.just(MESSAGE_DTO_A_INNER), MessageDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(MessageDto.class)
                .value(message -> assertEquals(MESSAGE_DTO_A_INNER, message));
    }

    @Test
    void getMobileLocation() {
        LastKnownLocation expected = getLastLocations().get(3);
        when(stationService.findLastLocation(any(), any())).thenReturn(Mono.just(expected));
        client.get().uri("/location/" + ID_INNER)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LastKnownLocation.class)
                .isEqualTo(expected);
    }
}