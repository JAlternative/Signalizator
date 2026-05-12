--liquibase formatted sql

--changeset shoroh:001-create-sources

create table sources (
    id bigserial primary key,
    code varchar(64) not null unique,
    name varchar(255) not null,
    base_url varchar(512),
    enabled boolean not null default true,
    created_at timestamptz not null default current_timestamp,
    updated_at timestamptz not null default current_timestamp
);

comment on table sources is 'Источники данных, справочник мест, откуда наша платформа умеет брать данные';
comment on column sources.id is 'Автоинкремент';
comment on column sources.code is 'Строковый код источника';
comment on column sources.name is 'Человекочитаемое имя источника';
comment on column sources.base_url is 'Базовый URL источника';
comment on column sources.enabled is 'Включён источник или нет';
comment on column sources.created_at is 'Дата создания';
comment on column sources.updated_at is 'Дата обновления';


--changeset shoroh:002-create-instruments

create table instruments (
    id bigserial primary key,
    source_id bigint not null,
    ticker varchar(64) not null,
    name varchar(255),
    board varchar(32) not null,
    market varchar(32),
    currency varchar(32),
    enabled boolean not null default true,
    created_at timestamptz not null default current_timestamp,
    updated_at timestamptz not null default current_timestamp,

    constraint fk_instruments_source
    foreign key (source_id) references sources(id),

    constraint uk_instruments_source_ticker_board
    unique (source_id, ticker, board)
);