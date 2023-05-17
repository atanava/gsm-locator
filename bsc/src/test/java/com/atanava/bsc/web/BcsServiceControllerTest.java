package com.atanava.bsc.web;

import com.atanava.bsc.dto.TriangleDto;
import com.atanava.bsc.helpers.TestData;
import com.atanava.bsc.service.BcsCacheHolder;
import com.atanava.bsc.service.data.Triangle;
import com.atanava.bsc.util.BaseStationConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collection;
import java.util.SortedSet;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BcsServiceControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private BcsCacheHolder meshHolder;

    @Disabled
    @Test
    void getBaseStationMesh() throws JsonProcessingException {
        SortedSet<Triangle> source = TestData.getBcsCacheHolder().getBaseStationMesh().getTriangles();
        Collection<TriangleDto> expected = source.stream().map(BaseStationConverter::triangleToDto).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        String expectedAsString = mapper.writeValueAsString(expected);
        when(meshHolder.getBaseStationMesh()).thenReturn(TestData.getBcsCacheHolder().getBaseStationMesh());

        client.get().uri("/service")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedAsString);
    }

}
