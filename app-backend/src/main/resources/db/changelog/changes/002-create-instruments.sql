--liquibase formatted sql

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

comment on table instruments is 'Инструменты, которые доступны в конкретном источнике данных';
comment on column instruments.id is 'Автоинкремент';
comment on column instruments.source_id is 'Ссылка на источник данных';
comment on column instruments.ticker is 'Тикер инструмента';
comment on column instruments.name is 'Название инструмента';
comment on column instruments.board is 'Режим торгов, например TQBR';
comment on column instruments.market is 'Рынок, например shares';
comment on column instruments.currency is 'Валюта инструмента, например RUB';
comment on column instruments.enabled is 'Включён инструмент или нет';
comment on column instruments.created_at is 'Дата создания';
comment on column instruments.updated_at is 'Дата обновления';