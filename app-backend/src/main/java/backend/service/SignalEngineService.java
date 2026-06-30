package backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignalEngineService {

    private final PriceChangeSignalService priceChangeSignalService;

    public void analyzeInstrument(Long instrumentId) {
        priceChangeSignalService.generateForInstrument(instrumentId);
    }
}
