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