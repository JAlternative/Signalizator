# Signalizator / Agent Platform — README-шпаргалка

## 1. Что мы строим

Сейчас активный этап проекта — интеграция с MOEX. Backend умеет брать список инструментов из БД, ходить в MOEX ISS API, получать рыночный snapshot, сохранять его в таблицу `price_points` и отдавать историю через API.

Главная цепочка:

```text
Instrument из БД
→ MoexIssClient
→ MOEX ISS API
→ raw JSON
→ MoexTableMapper
→ MoexMarketDataSnapshot
→ PricePoint
→ price_points
→ API истории
```

---

## 2. Таблицы

### `sources`

Справочник источников данных.

Сейчас главный источник:

```text
code = MOEX
name = Moscow Exchange ISS
base_url = https://iss.moex.com
```

### `instruments`

Справочник инструментов.

Сейчас стартовый watchlist:

```text
SBER
GAZP
LKOH
```

Главные поля:

```text
source_id — источник данных
ticker    — тикер, например SBER
board     — режим торгов, например TQBR
market    — рынок, например shares
enabled   — включён ли инструмент в watchlist
```

### `price_points`

История рыночных snapshot.

Одна строка = один сохранённый snapshot по одному инструменту.

Главные поля:

```text
instrument_id
sec_id
board_id
bid
offer
spread
open_price
low_price
high_price
last_price
waprice
change
num_trades
volume_today
value_today
trading_status
update_time
system_time
created_at
```

---

## 3. Liquibase

Файлы миграций:

```text
001-create-sources.sql
002-create-instruments.sql
003-seed-moex-and-watchlist.sql
004-create-price-points.sql
```

Главный файл:

```text
db.changelog-master.yaml
```

Правило: старые применённые миграции не переписываем, новые изменения добавляем новым changeset.

---

## 4. Entity

### `Source`

Отражает таблицу `sources`.

### `Instrument`

Отражает таблицу `instruments`.

Связан с `Source` через `source_id`.

### `PricePoint`

Отражает таблицу `price_points`.

Хранит один snapshot: bid, offer, last price, volume, status, время.

---

## 5. Repository

### `SourceRepository`

Ищет источники, например `MOEX`.

### `InstrumentRepository`

Ищет инструменты.

Главное:

```text
findByEnabledTrue()
findAllBySourceId(...)
```

### `PricePointRepository`

Сохраняет и читает историю.

Главное:

```text
findTop100ByInstrumentIdOrderBySystemTimeDesc(...)
```

---

## 6. MOEX integration

### `MoexIssClient`

Низкоуровневый HTTP-клиент.

Он строит URL:

```text
{baseUrl}/iss/engines/stock/markets/{market}/boards/{board}/securities/{ticker}.json
```

и возвращает raw JSON строкой.

### `MoexTableMapper`

MOEX отдаёт данные в формате таблицы:

```text
columns = ["SECID", "BOARDID", "BID", "OFFER", "LAST"]
data[0] = ["SBER", "TQBR", 322.86, 322.88, 322.88]
```

`MoexTableMapper` превращает это в объект.

### `MoexMarketDataParser`

Берёт из raw JSON таблицу `marketdata` и превращает первую строку в `MoexMarketDataSnapshot`.

### `MoexMarketDataSnapshot`

DTO snapshot от MOEX.

Поля:

```text
secId
boardId
bid
offer
spread
open
low
high
last
waprice
change
numTrades
volumeToday
valueToday
tradingStatus
updateTime
systemTime
```

---

## 7. Services

### `MarketDirectoryService`

Сервис справочников.

Умеет:

```text
получить источник MOEX
получить инструмент по id
получить enabled MOEX instruments
добавить инструмент в watchlist
```

### `MoexMarketDataService`

Сервис для одного инструмента.

Умеет:

```text
getRawSecurityDataByInstrumentId(...)
getMarketDataSnapshotByInstrumentId(...)
collectAndSaveSnapshot(...)
getPricePointHistory(...)
```

Главный метод сохранения:

```text
collectAndSaveSnapshot(instrumentId)
```

Цепочка:

```text
найти Instrument
→ получить snapshot от MOEX
→ создать PricePoint
→ сохранить в price_points
→ вернуть сохранённую строку
```

### `MoexWatchlistCollectorService`

Сервис для всего watchlist.

Умеет:

```text
collectEnabledMoexWatchlistOnce()
```

Цепочка:

```text
получить все enabled MOEX instruments
→ по каждому вызвать collectAndSaveSnapshot(...)
→ вернуть id сохранённых price_points
```

---

## 8. Controllers

### `SourceController`

```http
GET /api/sources
```

Возвращает источники данных.

### `InstrumentController`

```http
GET /api/instruments
GET /api/instruments/{id}
POST /api/instruments/watchlist
GET /api/instruments/{id}/price-points
```

Отвечает за инструменты и историю price points.

### `MoexDebugController`

```http
GET  /api/moex/instruments/{id}/raw
GET  /api/moex/instruments/{id}/snapshot
POST /api/moex/instruments/{id}/collect
POST /api/moex/watchlist/collect
```

Это временный debug-controller для проверки MOEX collector.

---

## 9. DTO

### `InstrumentResponse`

Ответ по инструменту.

### `SourceResponse`

Ответ по источнику.

### `PricePointResponse`

Ответ по истории price points.

### `AddInstrumentToWatchlistRequest`

Request body для добавления инструмента в watchlist:

```json
{
  "instrumentId": 1
}
```

---

## 10. Основные сценарии

### Посмотреть инструменты

```http
GET http://localhost:9094/api/instruments
```

### Получить snapshot без сохранения

```http
GET http://localhost:9094/api/moex/instruments/1/snapshot
```

### Сохранить snapshot по одному инструменту

```http
POST http://localhost:9094/api/moex/instruments/1/collect
```

Ответ — id новой строки в `price_points`.

### Сохранить snapshot по всему watchlist

```http
POST http://localhost:9094/api/moex/watchlist/collect
```

Ответ:

```json
[3, 4, 5]
```

Это id новых строк в `price_points`.

### Посмотреть историю инструмента

```http
GET http://localhost:9094/api/instruments/1/price-points
```

---

## 11. SQL для проверки

```sql
select
    pp.id,
    pp.instrument_id,
    i.ticker,
    pp.sec_id,
    pp.board_id,
    pp.last_price,
    pp.bid,
    pp.offer,
    pp.system_time,
    pp.created_at
from price_points pp
join instruments i on i.id = pp.instrument_id
order by pp.id desc
limit 10;
```

---

## 12. Что уже сделано

- Spring Boot backend.
- PostgreSQL datasource.
- Liquibase migrations.
- Таблицы `sources`, `instruments`, `price_points`.
- Seed MOEX + SBER / GAZP / LKOH.
- JPA entity и repository.
- MOEX HTTP client.
- Parser формата `columns + data`.
- Сохранение snapshot в `price_points`.
- Чтение истории snapshot.
- Ручной collector по одному инструменту.
- Ручной collector по всему watchlist.

---

## 13. Что дальше

Ближайшие следующие шаги:

1. Scheduler для автоматического запуска collector.
2. Настройка интервала scheduler через `application.yml`.
3. Логирование вместо `System.out`.
4. Signal engine на основе истории `price_points`.
5. UI на Jmix.
