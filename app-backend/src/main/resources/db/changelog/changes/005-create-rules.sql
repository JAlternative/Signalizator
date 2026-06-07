--liquibase formatted sql

--changeset shoroh:005-create-rules

create table rules (
    id bigserial primary key,
    code varchar(64) not null unique,
    name varchar(255) not null,
    description text,
    enabled boolean not null default true,
    created_at timestamptz not null default current_timestamp,
    updated_at timestamptz not null default current_timestamp
);

comment on table rules is 'Справочник правил signal engine, по которым платформа анализирует рыночные данные и создаёт сигналы';

comment on column rules.id is 'Автоинкрементный идентификатор правила';
comment on column rules.code is 'Стабильный технический код правила, используется в коде и при генерации сигналов';
comment on column rules.name is 'Человекочитаемое название правила';
comment on column rules.description is 'Описание логики правила и условий его срабатывания';
comment on column rules.enabled is 'Включено правило или нет';
comment on column rules.created_at is 'Дата и время создания правила';
comment on column rules.updated_at is 'Дата и время последнего обновления правила';