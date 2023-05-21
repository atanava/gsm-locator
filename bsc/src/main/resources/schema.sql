create schema if not exists test;

drop table if exists base_station;

create table if not exists base_station
(
    id           UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name         text  not null,
    x_coordinate float not null,
    y_coordinate float not null,
    radius       float,
    constraint x_y_name_index unique (x_coordinate, y_coordinate, name)
);

drop table if exists report;

create table if not exists report
(
    id        UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    base_id   UUID      not null,
    mobile_id UUID      not null,
    distance  float     not null,
    timestamp timestamp not null,
    constraint base_id_mobile_id_timestamp_index unique (base_id, mobile_id, timestamp)
);
