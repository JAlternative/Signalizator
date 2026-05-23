package backend.service;

import backend.integration.moex.MoexIssClient;
import backend.persistence.entity.Instrument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoexMarketDataService {

    private final MoexIssClient moexIssClient;
    private final MarketDirectoryService marketDirectoryService;

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
}
