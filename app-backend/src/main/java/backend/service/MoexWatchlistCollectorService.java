package backend.service;

import backend.persistence.entity.Instrument;
import backend.persistence.entity.PricePoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoexWatchlistCollectorService {

    private final MarketDirectoryService marketDirectoryService;
    private final MoexMarketDataService moexMarketDataService;
    private final PriceChangeSignalService priceChangeSignalService;

    /**
     * Собирает один snapshot по всему enabled MOEX watchlist.
     *
     * Логика:
     * 1. Берём все включённые MOEX-инструменты из БД.
     * 2. По каждому инструменту идём в MOEX.
     * 3. Сохраняем свежий snapshot в price_points.
     * 4. После сохранения пробуем создать сигнал по изменению цены.
     * 5. Возвращаем id сохранённых PricePoint.
     */
    public List<Long> collectEnabledMoexWatchlistOnce() {
        List<Instrument> instruments = marketDirectoryService.getEnabledMoexInstruments();
        List<Long> savedIds = new ArrayList<>();

        for (Instrument instrument : instruments) {
            moexMarketDataService.collectAndSaveSnapshot(instrument.getId())
                    .ifPresent(pricePoint -> {
                        savedIds.add(pricePoint.getId());
                        priceChangeSignalService.generateForInstrument(instrument.getId());
                    });
        }

        return savedIds;
    }
}