package com.atanava.bts.service;

import lombok.Value;

import java.util.UUID;

@Value
public class MobileStation {

    UUID id;

    Float lastKnownX;

    Float lastKnownY;
}
