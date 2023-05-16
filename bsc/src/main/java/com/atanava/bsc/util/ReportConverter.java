package com.atanava.bsc.util;

import com.atanava.bsc.dto.ReportDto;
import com.atanava.bsc.model.Report;

import java.util.UUID;

public class ReportConverter {

    public static Report dtoToReport(UUID baseId, ReportDto dto) {
        return Report.builder()
                .baseId(baseId)
                .mobileId(dto.getMobileId())
                .distance(dto.getDistance())
                .timestamp(dto.getTimestamp())
                .build();
    }
}
