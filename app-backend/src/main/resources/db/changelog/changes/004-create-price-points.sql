--liquibase formatted sql

--changeset shoroh:004-create-price-points

create table price_points (
    id bigserial primary key,
    instrument_id bigint not null,
    sec_id varchar(64) not null,
    board_id varchar(32) not null,
    bid numeric(19, 6),
    offer numeric(19, 6),
    spread numeric(19, 6),
    open_price numeric(19, 6),
    low_price numeric(19, 6),
    high_price numeric(19, 6),
    last_price numeric(19, 6),
    waprice numeric(19, 6),
    change numeric(19, 6),
    num_trades integer,
    volume_today bigint,
    value_today bigint,
    trading_status varchar(16),
    update_time time,
    system_time timestamp,
    created_at timestamptz not null default current_timestamp,

    constraint fk_price_points_instrument
    foreign key (instrument_id) references instruments(id)
);

create index idx_price_points_instrument_system_time
on price_points (instrument_id, system_time);

comment on table price_points is 'История рыночных снапшотов по инструментам';

comment on column price_points.id is 'Автоинкрементный идентификатор ценовой точки';
comment on column price_points.instrument_id is 'Ссылка на инструмент из таблицы instruments';
comment on column price_points.sec_id is 'Тикер инструмента в MOEX, например SBER';
comment on column price_points.board_id is 'Режим торгов MOEX, например TQBR';

comment on column price_points.bid is 'Лучшая цена покупки';
comment on column price_points.offer is 'Лучшая цена продажи';
comment on column price_points.spread is 'Разница между offer и bid';

comment on column price_points.open_price is 'Цена открытия торгового дня';
comment on column price_points.low_price is 'Минимальная цена за торговый день';
comment on column price_points.high_price is 'Максимальная цена за торговый день';
comment on column price_points.last_price is 'Последняя цена сделки';
comment on column price_points.waprice is 'Средневзвешенная цена';
comment on column price_points.change is 'Изменение цены';

comment on column price_points.num_trades is 'Количество сделок за текущий торговый день';
comment on column price_points.volume_today is 'Объём торгов за день в штуках';
comment on column price_points.value_today is 'Оборот торгов за день в деньгах';

comment on column price_points.trading_status is 'Статус торгов инструмента';
comment on column price_points.update_time is 'Время обновления данных на стороне MOEX';
comment on column price_points.system_time is 'Системное время снапшота из MOEX';
comment on column price_points.created_at is 'Дата и время сохранения снапшота в нашей системе';
