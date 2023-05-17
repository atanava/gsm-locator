package com.atanava.bsc.util;

import com.atanava.bsc.dto.BaseStationDto;
import com.atanava.bsc.dto.TriangleDto;
import com.atanava.bsc.model.BaseStation;
import com.atanava.bsc.service.data.BaseStationDefinition;
import com.atanava.bsc.service.data.Triangle;

public class BaseStationConverter {

    public static BaseStation dtoToBaseStation(BaseStationDto dto) {
        return BaseStation.builder()
                .name(dto.getName())
                .xCoordinate(dto.getLongitude().floatValue())
                .yCoordinate(dto.getLatitude().floatValue())
                .build();
    }

    public static TriangleDto triangleToDto(Triangle triangle) {
        return TriangleDto.builder()
                .pointA(definitionToDto((BaseStationDefinition) triangle.getPointA()))
                .pointB(definitionToDto((BaseStationDefinition) triangle.getPointB()))
                .pointC(definitionToDto((BaseStationDefinition) triangle.getPointC()))
                .build();
    }

    public static BaseStationDto definitionToDto(BaseStationDefinition definition) {
        return BaseStationDto
                .builder()
                .id(definition.getId().toString())
                .longitude(definition.getLongitude())
                .latitude(definition.getLatitude())
                .name(definition.getName())
                .detectionRadiusInMeters(definition.getDetectionRadiusInMeters())
                .build();
    }

    public static BaseStationDefinition baseStationToDefinition(BaseStation base) {
        return BaseStationDefinition.builder()
                .id(base.getId())
                .name(base.getName())
                .latitude(base.getXCoordinate().doubleValue())
                .longitude(base.getYCoordinate().doubleValue())
                .detectionRadiusInMeters(base.getDetectionRadiusInMeters())
                .build();
    }

}
