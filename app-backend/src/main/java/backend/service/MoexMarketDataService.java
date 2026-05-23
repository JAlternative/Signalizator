package backend.service;

import backend.integration.moex.MoexIssClient;
import backend.persistence.entity.Instrument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoexMarketDataService {

    private final MoexIssClient moexIssClient;
    private final MarketDirectoryService marketDirectoryService;

    public Optional<String> getRawSecurityDataByInstrumentId(Long instrumentId) {
        Instrument instrument = marketDirectoryService.getInstrumentById(instrumentId)
                .orElseThrow(() -> new IllegalArgumentException("Не найдено значение в таблице instrument по id: " + instrumentId));
        String response = moexIssClient.getSecurityRawJson(
                instrument.getSource().getBaseUrl(), instrument.getMarket(), instrument.getBoard(), instrument.getTicker());
        return Optional.of(response);
    }
}
