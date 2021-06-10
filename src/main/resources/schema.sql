drop table if exists customer;
create table if not exists customer
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

