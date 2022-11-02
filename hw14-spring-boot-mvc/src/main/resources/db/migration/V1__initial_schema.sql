
create table Client
(
    id    uuid        not null primary key,
    name  varchar(50) not null
);

create table Phone
(
    id           uuid        not null primary key,
    phone_number varchar(50) not null unique,
    client_id    uuid        not null references Client (id)
);
create unique index client_id_phone_number_idx on Phone (client_id, phone_number);

create table Address
(
    id        uuid         not null primary key,
    street    varchar(50)  not null unique,
    client_id uuid         not null references Client (id)
);

create unique index client_id_street_idx on Address (client_id, street);

