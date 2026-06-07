--liquibase formatted sql

--changeset shoroh:006-seed-mvp-rules

insert into public.rules (code, name, description, enabled)
values
    (
        'PRICE_CHANGE',
        'Изменение цены',
        'Правило срабатывает, когда последняя цена инструмента заметно изменилась относительно предыдущего снапшота',
        true
    ),
    (
        'VOLUME_SPIKE',
        'Рост объёма торгов',
        'Правило срабатывает, когда объём торгов по инструменту резко вырос относительно предыдущих значений',
        true
    ),
    (
        'SPREAD_WIDE',
        'Широкий спред',
        'Правило срабатывает, когда разница между лучшей ценой продажи и лучшей ценой покупки становится слишком большой',
        true
    )
on conflict (code) do update
    set name = excluded.name,
        description = excluded.description,
        enabled = excluded.enabled,
        updated_at = current_timestamp;

--rollback delete from public.rules where code in ('PRICE_CHANGE', 'VOLUME_SPIKE', 'SPREAD_WIDE');