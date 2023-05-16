package com.atanava.bsc.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table
@Getter
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(exclude = {"id", "detectionRadiusInMeters"})
public class BaseStation {
    @Id
    UUID id;
    String name;
    Float xCoordinate;
    Float yCoordinate;
    @Column("radius")
    Float detectionRadiusInMeters;
}
