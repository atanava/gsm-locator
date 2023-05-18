package com.atanava.bts.service;

import com.atanava.bts.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MessageGenerator implements MessageProvider {

    private Float maxDistance = Float.MAX_VALUE;
    private Float minDistance = 0F;

    @Override
    public List<MessageDto> getMessageList(List<MobileStation> mobiles, List<BaseStation> bases) {
        return this.generateMessages(mobiles, bases);
    }

    public List<MessageDto> generateMessages(List<MobileStation> mobiles, List<BaseStation> bases) {
        return mobiles.stream()
                .map(mob -> new MessageDto(
//                        mob.getId(),
//                        findAvailableBaseStations(mob, bases).stream()
//                                .collect(Collectors.toMap(BaseStation::getId, (b) -> {
//                                    try {
//                                        return calculateDistance(b, mob);
//                                    } catch (Exception e) {
//                                        log.error("Unable to calculate distance: {}; Base coordinates: X={} Y={}; Mobile coordinates: X={} Y={}",
//                                                e.getMessage(), b.getXCoordinate(), b.getYCoordinate(), mob.getLastKnownX(), mob.getLastKnownY());
//                                        return -1.0F;
//                                    }
//                                }))
                ))
                .collect(Collectors.toList());
    }

    private List<BaseStation> findAvailableBaseStations(MobileStation mobileStation, List<BaseStation> bases) {
        return bases.stream()
                .filter(bs -> bs.getDetectionArea().contains(mobileStation.getLastKnownX(), mobileStation.getLastKnownY()))
                .collect(Collectors.toList());
    }

    private Float calculateDistance(BaseStation baseStation, MobileStation mobileStation) {
        Ellipse2D.Float detectionArea = baseStation.getDetectionArea();
        return Optional.of((float) Point2D.distance(
                        detectionArea.getCenterX(), detectionArea.getCenterY(),
                        mobileStation.getLastKnownX(), mobileStation.getLastKnownY()))
                .filter(distance -> distance < maxDistance && distance > minDistance)
                .orElseThrow(() -> new IllegalArgumentException("Distance out of range"));
    }

}
