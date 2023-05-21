package com.atanava.bsc.service;

import com.atanava.bsc.dto.BaseStationDto;
import com.atanava.bsc.util.GeometryUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Tag("integration")
@SpringBootTest
class BaseStationDefinitionProviderTest {

    @Value("${osm.uri.basic}")
    private String uri;
    @Value("${osm.uri.params.type}")
    private String[] types;
    @Value("${osm.uri.params.location}")
    private String location;
    @Value("${osm.uri.params.format}")
    private String format;
    @Value("${base.detection.minDistance}")
    private float minDistance;
    @Value("${base.detection.maxDistance}")
    private float maxDistance;
    @Value("${base.mesh.maxSize}")
    private int maxMeshSize;

    @Autowired
    private BaseStationDefinitionProvider provider;

    @Test
    void loadProps() {
        System.out.println("uri: " + uri);
        System.out.println("types: " + Arrays.toString(types));
        System.out.println("location: " + location);
        System.out.println("format: " + format);
        System.out.println("minDistance: " + minDistance);
        System.out.println("maxDistance: " + maxDistance);
        System.out.println("maxMeshSize: " + maxMeshSize);
    }

    /** Requires internet connection and Nominatim OSM service availability */
    @Test
    void findFakeBaseStations() {
        List<BaseStationDto> stations = Objects.requireNonNull(provider.findFakeBaseStations().collectList().block());
        double minX = stations.stream().mapToDouble(BaseStationDto::getLatitude).min().orElse(0);
        double maxX = stations.stream().mapToDouble(BaseStationDto::getLatitude).max().orElse(0);
        double minY = stations.stream().mapToDouble(BaseStationDto::getLongitude).min().orElse(0);
        double maxY = stations.stream().mapToDouble(BaseStationDto::getLongitude).max().orElse(0);
        System.out.println("Width: " + (maxX - minX));
        System.out.println("Height: " + (maxY - minY));
        System.out.println("Distance in meters: " + new Double(GeometryUtil.distanceInMeters(minX, maxX, minY, maxY, 0, 0)).floatValue());
        System.out.println("Width in meters: " + new Double(GeometryUtil.distanceInMeters(minX, maxX, minY, minY, 0, 0)).floatValue());
        System.out.println("Height in meters: " + new Double(GeometryUtil.distanceInMeters(minX, minX, minY, maxY, 0, 0)).floatValue());
        System.out.println("Hotels: " + stations.stream().filter(b -> "hotel".equalsIgnoreCase(b.getLocationType())).count());
        System.out.println("Supermarkets: " + stations.stream().filter(b -> "supermarket".equalsIgnoreCase(b.getLocationType())).count());
    }

}
