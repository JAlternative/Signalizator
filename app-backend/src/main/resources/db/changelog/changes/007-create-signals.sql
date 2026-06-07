
--liquibase formatted sql

--changeset shoroh:007-create-signals

create table signals
(
    id             bigserial primary key,
    instrument_id  bigint       not null,
    rule_id        bigint       not null,
    price_point_id bigint,
    type           varchar(64)  not null,
    severity       varchar(32)  not null,
    title          varchar(255) not null,
    explanation    text         not null,
    facts_json     jsonb,
    status         varchar(32)  not null default 'NEW',
    dedup_key      varchar(255) not null,
    created_at     timestamptz  not null default current_timestamp,
    updated_at     timestamptz  not null default current_timestamp,

    constraint fk_signals_instrument
        foreign key (instrument_id) references instruments (id),

    constraint fk_signals_rule
        foreign key (rule_id) references rules (id),

    constraint fk_signals_price_point
        foreign key (price_point_id) references price_points (id),

    constraint uk_signals_dedup_key
        unique (dedup_key)
);

create index idx_signals_instrument_created_at
    on signals (instrument_id, created_at);

create index idx_signals_status_created_at
    on signals (status, created_at);

comment on table signals is 'Сигналы, созданные signal engine на основе рыночных данных и правил';

comment on column signals.id is 'Автоинкрементный идентификатор сигнала';
comment on column signals.instrument_id is 'Ссылка на инструмент, по которому создан сигнал';
comment on column signals.rule_id is 'Ссылка на правило, которое создало сигнал';
comment on column signals.price_point_id is 'Ссылка на ценовую точку, на основе которой создан сигнал';
comment on column signals.type is 'Технический тип сигнала, например PRICE_CHANGE, VOLUME_SPIKE или SPREAD_WIDE';
comment on column signals.severity is 'Важность сигнала, например LOW, MEDIUM или HIGH';
comment on column signals.title is 'Короткий человекочитаемый заголовок сигнала';
comment on column signals.explanation is 'Подробное объяснение, почему сигнал был создан';
comment on column signals.facts_json is 'Фактические данные в JSON, на основании которых сработало правило';
comment on column signals.status is 'Статус сигнала, например NEW, ACK или MUTED';
comment on column signals.dedup_key is 'Ключ дедупликации, который не даёт создавать одинаковые сигналы повторно';
comment on column signals.created_at is 'Дата и время создания сигнала';
comment on column signals.updated_at is 'Дата и время последнего обновления сигнала';

--rollback drop table if exists signals;