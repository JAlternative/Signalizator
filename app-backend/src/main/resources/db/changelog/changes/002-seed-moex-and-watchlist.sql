--liquibase formatted sql

--changeset shoroh:003-seed-moex-and-watchlist

insert into public.sources (code, name, base_url, enabled)
values ('MOEX', 'Moscow Exchange ISS', 'https://iss.moex.com', true)
on conflict (code) do update
set name = excluded.name,
    base_url = excluded.base_url,
    enabled = excluded.enabled,
    updated_at = current_timestamp;

insert into public.instruments (source_id, ticker, name, board, market, currency, enabled)
select s.id, v.ticker, v.name, v.board, v.market, v.currency, true
from public.sources s
join (
    values
        ('SBER', 'Сбербанк', 'TQBR', 'shares', 'RUB'),
        ('GAZP', 'Газпром', 'TQBR', 'shares', 'RUB'),
        ('LKOH', 'Лукойл', 'TQBR', 'shares', 'RUB')
) as v(ticker, name, board, market, currency)
    on true
where s.code = 'MOEX'
on conflict (source_id, ticker, board) do update
set name = excluded.name,
    market = excluded.market,
    currency = excluded.currency,
    enabled = excluded.enabled,
    updated_at = current_timestamp;

--rollback delete from public.instruments where source_id = (select id from public.sources where code = 'MOEX') and ticker in ('SBER', 'GAZP', 'LKOH') and board = 'TQBR';
--rollback delete from public.sources where code = 'MOEX';