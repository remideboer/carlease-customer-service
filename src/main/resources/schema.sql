create schema if not exists customer_service;
use customer_service;
drop table if exists klant;
create table if not exists klant
(
    id             int auto_increment primary key,
    voornaam       varchar(80)  not null,
    tussenvoegsel  varchar(30),
    achternaam     varchar(80)  not null,
    straat_naam    varchar(80),
    postcode       varchar(6),
    huisnummer     int,
    toevoeging     varchar(8),
    woonplaats     varchar(40),
    emailadres     varchar(254) not null,
    telefoonnummer varchar(15)  not null
);

