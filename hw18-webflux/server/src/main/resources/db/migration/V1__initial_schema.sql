create table client
(
    id    uuid        not null primary key,
    name  varchar(50) not null,
    street    varchar(50)  not null unique,
    phone varchar(50) not null unique
);