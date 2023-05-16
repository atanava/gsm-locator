package com.atanava.bsc.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Table
@Getter
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(exclude = {"id"})
public class Report {
    @Id
    UUID id;
    UUID baseId;
    UUID mobileId;
    Float distance;
    Timestamp timestamp;
}
