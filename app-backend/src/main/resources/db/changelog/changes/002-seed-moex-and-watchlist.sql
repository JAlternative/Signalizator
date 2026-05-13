--liquibase formatted sql

--shoroh:003-seed-moex-and-watchlist

INSERT INTO public.sources
(code, "name", base_url, enabled)
VALUES('MOEX', 'Moscow Exchange ISS', 'https://iss.moex.com', true);


INSERT INTO public.instruments
(source_id, ticker, "name", board, market, currency, enabled)
VALUES
(1, 'SBER', 'Сбербанк', 'TQBR', 'shares', 'RUB', true),
(1, 'GAZP', 'Газпром', 'TQBR', 'shares', 'RUB', true),
(1, 'LKOH', 'Лукойл', 'TQBR', 'shares', 'RUB', true);