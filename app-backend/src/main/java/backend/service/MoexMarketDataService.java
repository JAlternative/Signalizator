package backend.service;

import backend.integration.moex.MoexIssClient;
import backend.integration.moex.MoexMarketDataParser;
import backend.integration.moex.dto.MoexMarketDataSnapshot;
import backend.persistence.entity.Instrument;
import backend.persistence.entity.PricePoint;
import backend.persistence.repository.PricePointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static backend.utils.Utils.parseLocalDateTime;
import static backend.utils.Utils.parseLocalTime;

@Service
@RequiredArgsConstructor
public class MoexMarketDataService {

    private final MoexIssClient moexIssClient;
    private final MarketDirectoryService marketDirectoryService;
    private final MoexMarketDataParser moexMarketDataParser;
    private final PricePointRepository pricePointRepository;

    @Transactional(readOnly = true)
    public Optional<String> getRawSecurityDataByInstrumentId(Long instrumentId) {
        Optional<Instrument> instrumentOptional = marketDirectoryService.getInstrumentById(instrumentId);

        if (instrumentOptional.isEmpty()) {
            return Optional.empty();
        }

        Instrument instrument = instrumentOptional.get();

        String response = moexIssClient.getSecurityRawJson(
                instrument.getSource().getBaseUrl(),
                instrument.getMarket(),
                instrument.getBoard(),
                instrument.getTicker()
        );

        return Optional.ofNullable(response);
    }

    @Transactional(readOnly = true)
    public Optional<MoexMarketDataSnapshot> getMarketDataSnapshotByInstrumentId(Long instrumentId) {
        Optional<String> rawJson = getRawSecurityDataByInstrumentId(instrumentId);

        if (rawJson.isEmpty()) {
            return Optional.empty();
        }

        return moexMarketDataParser.parseMarketDataSnapshot(rawJson.get());
    }

    /**
     * 1L
     * -> нашёл Instrument с id = 1
     * -> взял у него source / market / board / ticker
     * -> сходил в MOEX
     * -> получил raw JSON
     * -> распарсил в MoexMarketDataSnapshot
     * -> переложил в PricePoint
     * -> сохранил в price_points
     * */
    @Transactional
    public Optional<PricePoint> collectAndSaveSnapshot(Long instrumentId) {
        Optional<Instrument> instrumentOptional = marketDirectoryService.getInstrumentById(instrumentId);

        if (instrumentOptional.isEmpty()) {
            return Optional.empty();
        }

        Instrument instrument = instrumentOptional.get();

        Optional<MoexMarketDataSnapshot> marketDataSnapshot =
                getMarketDataSnapshotByInstrumentId(instrument.getId());

        if (marketDataSnapshot.isEmpty()) {
            return Optional.empty();
        }

        PricePoint pricePoint = toPricePoint(instrument, marketDataSnapshot.get());
        PricePoint saved = pricePointRepository.save(pricePoint);

        return Optional.of(saved);
    }

    private PricePoint toPricePoint(Instrument instrument, MoexMarketDataSnapshot snapshot) {
        PricePoint pricePoint = new PricePoint();

        pricePoint.setInstrument(instrument);
        pricePoint.setSecId(snapshot.secId());
        pricePoint.setBoardId(snapshot.boardId());

        pricePoint.setBid(snapshot.bid());
        pricePoint.setOffer(snapshot.offer());
        pricePoint.setSpread(snapshot.spread());

        pricePoint.setOpenPrice(snapshot.open());
        pricePoint.setLowPrice(snapshot.low());
        pricePoint.setHighPrice(snapshot.high());
        pricePoint.setLastPrice(snapshot.last());
        pricePoint.setWaprice(snapshot.waprice());
        pricePoint.setChange(snapshot.change());

        pricePoint.setNumTrades(snapshot.numTrades());
        pricePoint.setVolumeToday(snapshot.volumeToday());
        pricePoint.setValueToday(snapshot.valueToday());

        pricePoint.setTradingStatus(snapshot.tradingStatus());
        pricePoint.setUpdateTime(parseLocalTime(snapshot.updateTime()));
        pricePoint.setSystemTime(parseLocalDateTime(snapshot.systemTime()));

        return pricePoint;
    }

    @Transactional(readOnly = true)
    public List<PricePoint> getPricePointHistory(Long instrumentId) {
        return pricePointRepository.findTop100ByInstrumentIdOrderBySystemTimeDesc(instrumentId);
    }
}
